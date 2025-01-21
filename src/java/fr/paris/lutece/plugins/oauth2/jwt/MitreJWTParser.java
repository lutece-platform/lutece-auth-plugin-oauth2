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

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JWSAlgorithm;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import fr.paris.lutece.plugins.oauth2.business.AuthClientConf;
import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.business.IDToken;
import fr.paris.lutece.plugins.oauth2.business.Token;
import fr.paris.lutece.plugins.oauth2.web.Constants;
import io.jsonwebtoken.lang.Collections;

import org.apache.log4j.Logger;

import java.text.ParseException;

import java.util.Date;
import java.util.HashSet;

/**
 * MitreJWTParser
 *
 * Some parts of the code come from the MITRE project and especialy from the class : com.plec.artistes.security.CustomOIDCAuthenticationFilter
 */
public class MitreJWTParser implements JWTParser
{
    // Allow for time sync issues by having a window of X seconds.
    private int _nTimeSkewAllowance = 300;

    /**
     * {@inheritDoc }
     */
    @Override
    public void parseJWT( Token token, AuthClientConf clientConfig, AuthServerConf serverConfig, String strStoredNonce, Logger logger )
            throws TokenValidationException
    {
        JWT jwt;
        IDToken idToken = new IDToken( );

        try
        {
            jwt = com.nimbusds.jwt.JWTParser.parse( token.getIdTokenString( ) );
        }
        catch( ParseException ex )
        {
            throw new TokenValidationException( "Unable to parse JWT : " + ex.getMessage( ), ex );
        }

        // validate our ID Token over a number of tests
        ReadOnlyJWTClaimsSet idClaims;

        try
        {
            idClaims = jwt.getJWTClaimsSet( );
        }
        catch( ParseException ex )
        {
            throw new TokenValidationException( "Unable to get Claims set from JWT : " + ex.getMessage( ), ex );
        }

        Algorithm tokenAlg = jwt.getHeader( ).getAlgorithm( );

        Algorithm clientAlg = clientConfig.getIdTokenSignedResponseAlg( );

        if ( clientAlg != null )
        {
            if ( !clientAlg.equals( tokenAlg ) )
            {
                throw new TokenValidationException( "Token algorithm " + tokenAlg + " does not match expected algorithm " + clientAlg );
            }
        }

        if ( jwt instanceof PlainJWT )
        {
            logger.debug( "ID token is a Plain JWT" );

            if ( clientAlg == null )
            {
                throw new TokenValidationException( "Unsigned ID tokens can only be used if explicitly configured in client." );
            }

            if ( ( tokenAlg != null ) && !tokenAlg.equals( JWSAlgorithm.NONE ) )
            {
                throw new TokenValidationException( "Unsigned token received, expected signature with " + tokenAlg );
            }
        }
        else
            if ( jwt instanceof SignedJWT )
            {
                logger.debug( "ID token is a signed JWT" );

                /*
                 * // check the signature JwtSigningAndValidationService jwtValidator = null;
                 * 
                 * SignedJWT signedIdToken = (SignedJWT) idToken;
                 * 
                 * if (tokenAlg.equals(JWSAlgorithm.HS256) || tokenAlg.equals(JWSAlgorithm.HS384) || tokenAlg.equals(JWSAlgorithm.HS512)) {
                 * 
                 * // generate one based on client secret jwtValidator = symmetricCacheService.getSymmetricValidtor( clientConfig.getClient()); } else { //
                 * otherwise load from the server's public key jwtValidator = validationServices.getValidator( serverConfig.getJwksUri() ); }
                 * 
                 * if (jwtValidator != null) { if (!jwtValidator.validateSignature(signedIdToken)) { throw new
                 * TokenValidationException("Signature validation failed"); } } else {
                 * _logger.error("No validation service found. Skipping signature validation"); throw new
                 * TokenValidationException("Unable to find an appropriate signature validator for ID Token."); }
                 */
            } // TODO: encrypted id tokens

        // check the issuer
        if ( idClaims.getIssuer( ) == null )
        {
            throw new TokenValidationException( "Id Token Issuer is null" );
        }
        else
            if ( !idClaims.getIssuer( ).equals( serverConfig.getIssuer( ) ) )
            {
                throw new TokenValidationException( "Issuers do not match, expected " + serverConfig.getIssuer( ) + " got " + idClaims.getIssuer( ) );
            }

        // check expiration
        if ( idClaims.getExpirationTime( ) == null )
        {
            throw new TokenValidationException( "Id Token does not have required expiration claim" );
        }
        else
        {
            // it's not null, see if it's expired
            Date now = new Date( System.currentTimeMillis( ) - ( _nTimeSkewAllowance * 1000 ) );

            if ( now.after( idClaims.getExpirationTime( ) ) )
            {
                throw new TokenValidationException( "Id Token is expired: " + idClaims.getExpirationTime( ) );
            }
        }

        // check not before
        if ( idClaims.getNotBeforeTime( ) != null )
        {
            Date now = new Date( System.currentTimeMillis( ) + ( _nTimeSkewAllowance * 1000 ) );

            if ( now.before( idClaims.getNotBeforeTime( ) ) )
            {
                throw new TokenValidationException( "Id Token not valid untill: " + idClaims.getNotBeforeTime( ) );
            }
        }

        // check issued at
        if ( idClaims.getIssueTime( ) == null )
        {
            throw new TokenValidationException( "Id Token does not have required issued-at claim" );
        }
        else
        {
            // since it's not null, see if it was issued in the future
            Date now = new Date( System.currentTimeMillis( ) + ( _nTimeSkewAllowance * 1000 ) );

            if ( now.before( idClaims.getIssueTime( ) ) )
            {
                throw new TokenValidationException( "Id Token was issued in the future: " + idClaims.getIssueTime( ) );
            }
        }

        // check audience
        if ( idClaims.getAudience( ) == null )
        {
            throw new TokenValidationException( "Id token audience is null" );
        }
        else
            if ( !idClaims.getAudience( ).contains( clientConfig.getClientId( ) ) )
            {
                throw new TokenValidationException( "Audience does not match, expected " + clientConfig.getClientId( ) + " got " + idClaims.getAudience( ) );
            }

        // compare the nonce to our stored claim
        String strNonce = null;

        try
        {
            strNonce = idClaims.getStringClaim( "nonce" );
        }
        catch( ParseException ex )
        {
            throw new TokenValidationException( "ID token did not contain a nonce claim." );
        }

        if ( ( strNonce == null ) || strNonce.equals( "" ) )
        {
            logger.error( "ID token did not contain a nonce claim." );

            throw new TokenValidationException( "ID token did not contain a nonce claim." );
        }

        if ( !strNonce.equals( strStoredNonce ) )
        {
            logger.error( "Possible replay attack detected! The comparison of the nonce in the returned " + "ID Token to the session "
                    + Constants.NONCE_SESSION_VARIABLE + " failed. Expected " + strStoredNonce + " got " + strNonce + "." );

            throw new TokenValidationException( "Possible replay attack detected! The comparison of the nonce in the returned " + "ID Token to the session "
                    + Constants.NONCE_SESSION_VARIABLE + " failed. Expected " + strStoredNonce + " got " + strNonce + "." );
        }

        logger.debug( "Nonce has been validated" );

        // Get IDP
        String strIdp;

        try
        {
            strIdp = idClaims.getStringClaim( "idp" );
        }
        catch( ParseException ex )
        {
            throw new TokenValidationException( "ID token did not contain an idp claim.", ex );
        }

        // Get ACR
        String strAcr;

        try
        {
            strAcr = idClaims.getStringClaim( "acr" );
        }
        catch( ParseException ex )
        {
            throw new TokenValidationException( "ID token did not contain an acr claim.", ex );
        }

        idToken.setNonce( strNonce );
        idToken.setSubject( idClaims.getSubject( ) );
        idToken.setIdProvider( strIdp );
        idToken.setExpiration( String.valueOf( idClaims.getExpirationTime( ).getTime( ) / 1000L ) );
        idToken.setIssueAt( String.valueOf( idClaims.getIssueTime( ).getTime( ) / 1000L ) );
        idToken.setIssuer( idClaims.getIssuer( ) );
        idToken.setAudience( Collections.immutable( new HashSet<>( idClaims.getAudience( ) ) ) );
        idToken.setAcr( strAcr );
        logger.debug( "ID Token retrieved : " + idToken );

        token.setIdToken( idToken );
    }
}
