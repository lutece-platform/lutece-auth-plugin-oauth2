/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.oauth2.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.oauth2.business.AuthClientConf;
import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.business.IDToken;
import fr.paris.lutece.plugins.oauth2.business.Token;
import fr.paris.lutece.plugins.oauth2.service.CachingHttpAccessService;
import fr.paris.lutece.plugins.oauth2.web.Constants;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessService;
import fr.paris.lutece.util.httpaccess.PropertiesHttpClientConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

/**
 * Jjwt JWTParser
 */
public class JjwtJWTParser implements JWTParser
{
    private final Map<String, KeyLocator> _keyLocatorsMap = new ConcurrentHashMap<>( );
    private final HttpAccess _httpAccess;

    public JjwtJWTParser( )
    {
        HttpAccessService accessService = new CachingHttpAccessService( new PropertiesHttpClientConfiguration( ) );
        this._httpAccess = new HttpAccess( accessService );
    }

    private KeyLocator getKeyLocator( String strwksEndpointUri )
    {
        return _keyLocatorsMap.computeIfAbsent( strwksEndpointUri, uri -> new KeyLocator( uri, _httpAccess ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void parseJWT( Token token, AuthClientConf clientConfig, AuthServerConf serverConfig, String strStoredNonce, Logger logger )
            throws TokenValidationException
    {
        String strCompactJwt = token.getIdTokenString( );

        try
        {
            Claims claims = getClaims( strCompactJwt, clientConfig, serverConfig );

            IDToken idToken = new IDToken( );
            idToken.setAudience( claims.getAudience( ) );
            idToken.setIssuer( claims.getIssuer( ) );
            idToken.setSubject( claims.getSubject( ) );

            // Claims that should be verified
            idToken.setNonce( getVerifiedNonce( claims, strStoredNonce ) );
            idToken.setExpiration( getExpiration( claims ) );
            idToken.setIssueAt( getIssueAt( claims ) );

            // Extra claims for Oauth2
            idToken.setIdProvider( (String) claims.get( Constants.CLAIM_IDP ) );
            idToken.setAcr( (String) claims.get( Constants.CLAIM_ACR ) );

            logger.debug( "ID Token retrieved by JJWT parser implementation : " + idToken );

            token.setIdToken( idToken );
        }
        catch( SignatureException ex )
        {
            throw new TokenValidationException( ex.getMessage( ), ex );
        }
        catch( ExpiredJwtException ex )
        {
            throw new TokenValidationException( ex.getMessage( ), ex );
        }
        catch( IllegalArgumentException ex )
        {
            throw new TokenValidationException( ex.getMessage( ), ex );
        }
        catch( MalformedJwtException ex )
        {
            throw new TokenValidationException( ex.getMessage( ), ex );
        }
        catch( UnsupportedJwtException ex )
        {
            throw new TokenValidationException( ex.getMessage( ), ex );
        }
    }

    /**
     * Retrieve and check the nonce
     * 
     * @param claims
     *            Claims set
     * @param strStoredNonce
     *            The stored nonce
     * @return The verified nonce
     * @throws TokenValidationException
     *             if the nonce is not valid
     */
    private String getVerifiedNonce( Claims claims, String strStoredNonce ) throws TokenValidationException
    {
        // Check nonce
        String strNonce = (String) claims.get( Constants.CLAIM_NONCE );

        if ( strNonce == null )
        {
            throw new TokenValidationException( "The token doesn't contains the nonce info." );
        }

        if ( !strNonce.equals( strStoredNonce ) )
        {
            throw new TokenValidationException( "The nonce info has not the value expected." );
        }

        return strNonce;
    }

    /**
     * Retrieve the expiration date
     * 
     * @param claims
     *            Claims set
     * @return The expiration date
     */
    private String getExpiration( Claims claims )
    {
        long lExpiration = claims.getExpiration( ).getTime( );

        return String.valueOf( lExpiration / 1000L );
    }

    /**
     * Retrieve the issue at date
     * 
     * @param claims
     *            Claims set
     * @return The issue at date
     */
    private String getIssueAt( Claims claims )
    {
        long lIssueAt = claims.getIssuedAt( ).getTime( );

        return String.valueOf( lIssueAt / 1000L );
    }

    @Override
    public String parseJWT( String strJwt, AuthClientConf clientConfig, AuthServerConf serverConfig, Logger logger ) throws TokenValidationException
    {
        String strClaims;

        try
        {
            Claims claims = getClaims( strJwt, clientConfig, serverConfig );
            strClaims = new ObjectMapper( ).writeValueAsString( claims );
        } catch ( TokenValidationException | JsonProcessingException e )
        {
            throw new TokenValidationException( e.getMessage( ), e );
        }

        return strClaims;
    }
    
    /**
     * Get claims
     * @param strCompactJwt
     * @param clientConfig
     * @param serverConfig
     * @return claims
     * @throws TokenValidationException
     */
    private Claims getClaims ( String strCompactJwt, AuthClientConf clientConfig, AuthServerConf serverConfig) throws TokenValidationException
    {
        JwtParserBuilder parserBuilder = Jwts.parser( );

        if ( serverConfig.getJwksEndpointUri( ) != null )
        {
            parserBuilder.keyLocator( getKeyLocator( serverConfig.getJwksEndpointUri( ) ) );
        }
        else
        {
            parserBuilder.verifyWith( Keys.hmacShaKeyFor( clientConfig.getClientSecret( ).getBytes( StandardCharsets.UTF_8 ) ) );
        }

        JwtParser parser = parserBuilder.build( );
        Claims claims;
        if ( serverConfig == null || serverConfig.getIDTokenSignatureAlgorithmNames( ) == null )
        {
            // claims should be unsigned
            Jwt<Header, Claims> jwt = parser.parse( strCompactJwt ).accept( Jwt.UNSECURED_CLAIMS );
            claims = jwt.getPayload( );
        }
        else
        {
            // claims should be signed
            Jws<Claims> jws = parser.parse( strCompactJwt ).accept( Jws.CLAIMS );
            if ( !serverConfig.getIDTokenSignatureAlgorithmNames( ).contains( jws.getHeader( ).getAlgorithm( ) ) )
            {
                throw new TokenValidationException( "Expected alg is one of <" + serverConfig.getIDTokenSignatureAlgorithmNames( ) + "> but got <" + jws.getHeader( ).getAlgorithm( ) + ">" );
            }
            claims = jws.getPayload( );
        }
        
        return claims;
    }
}
