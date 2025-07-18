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
package fr.paris.lutece.plugins.oauth2.service.jwt;

import fr.paris.lutece.plugins.oauth2.business.AuthClientConf;
import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.business.Token;
import fr.paris.lutece.plugins.oauth2.jwt.JjwtJWTParser;
import fr.paris.lutece.plugins.oauth2.jwt.TokenValidationException;
import fr.paris.lutece.plugins.oauth2.web.Constants;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.fail;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Jjwt JWTParser Test
 */
public class JjwtJWTParserTest
{
    private static final String SECRET = "7504f9f0ef08473a4c26873e9c1b898e567a39e6b76b7e60e93a0cb25cae5eb8";
    private static final String AUDIENCE = "895fae591ccae777094931e269e46447";
    private static final String ISSUER = "http://franceconnect.gouv.fr";
    private static final String SUBJECT = "YWxhY3JpdMOp";
    private static final String IDP = "dgfip";
    private static final String NONCE = "12344354597459";
    private static final String ACR = "eidas2";

    @BeforeClass
    public static void initLutece( )
    {
        // fake initialization
        try
        {
            AppPathService.init( "" );
            AppPropertiesService.init( "" );
        }
        catch ( Exception e )
        {
            // ignore
        }
    }

    /**
     * Test of parseJWT method, of class JjwtJWTParser.
     */
    @Test
    public void testParseJWT( ) throws Exception
    {
        System.out.println( "parseJWT" );

        Token token = new Token( );
        token.setIdTokenString( buildJWT( ) );

        AuthClientConf clientConfig = new AuthClientConf( );
        clientConfig.setClientSecret( SECRET );

        AuthServerConf serverConfig = new AuthServerConf( );
        serverConfig.setIDTokenSignatureAlgorithmNames( Set.of( "HS512" ) );
        String strStoredNonce = NONCE;
        Logger logger = Logger.getLogger( Constants.LOGGER_OAUTH2 );
        JjwtJWTParser instance = new JjwtJWTParser( );
        instance.parseJWT( token, clientConfig, serverConfig, strStoredNonce, logger );

        System.out.print( token.getIdToken( ) );
    }

    @Test
    public void testParseJWTWrongAlg( ) throws Exception
    {
        System.out.println( "parseJWT" );

        Token token = new Token( );
        token.setIdTokenString( buildJWT( ) );

        AuthClientConf clientConfig = new AuthClientConf( );
        clientConfig.setClientSecret( SECRET );

        AuthServerConf serverConfig = new AuthServerConf( );
        serverConfig.setIDTokenSignatureAlgorithmNames( Set.of( "HS256" ) );
        String strStoredNonce = NONCE;
        Logger logger = Logger.getLogger( Constants.LOGGER_OAUTH2 );
        JjwtJWTParser instance = new JjwtJWTParser( );
        try
        {
            instance.parseJWT( token, clientConfig, serverConfig, strStoredNonce, logger );
        }
        catch( TokenValidationException e )
        {
            // ok
            return;
        }
        fail( "Validation should have failed" );
    }

    /**
     * Test of parseJWT method, of class JjwtJWTParser.
     */
    @Test
    public void testParseJWTInvalidSecret( ) throws Exception
    {
        Token token = new Token( );
        token.setIdTokenString( buildJWT( SECRET + "OTHER" ) );

        AuthClientConf clientConfig = new AuthClientConf( );
        clientConfig.setClientSecret( SECRET );

        AuthServerConf serverConfig = new AuthServerConf( );
        String strStoredNonce = NONCE;
        Logger logger = Logger.getLogger( Constants.LOGGER_OAUTH2 );
        JjwtJWTParser instance = new JjwtJWTParser( );
        try
        {
            instance.parseJWT( token, clientConfig, serverConfig, strStoredNonce, logger );
        }
        catch( TokenValidationException e )
        {
            // ok
            return;
        }
        fail( "Validation should have failed" );
    }

    /**
     * Test of parseJWT method, of class JjwtJWTParser.
     */
    @Test
    public void testParseJWTNotSigned( ) throws Exception
    {
        Token token = new Token( );
        token.setIdTokenString( buildJWT( SECRET, false ) );

        AuthClientConf clientConfig = new AuthClientConf( );
        clientConfig.setClientSecret( SECRET );

        AuthServerConf serverConfig = new AuthServerConf( );
        serverConfig.setIDTokenSignatureAlgorithmNames( Set.of( "HS512" ) );
        String strStoredNonce = NONCE;
        Logger logger = Logger.getLogger( Constants.LOGGER_OAUTH2 );
        JjwtJWTParser instance = new JjwtJWTParser( );
        try
        {
            instance.parseJWT( token, clientConfig, serverConfig, strStoredNonce, logger );
        }
        catch( TokenValidationException e )
        {
            // ok
            return;
        }
        fail( "Validation should have failed" );
    }

    private String buildJWT( )
    {
        return buildJWT( SECRET );
    }

    private String buildJWT( String secret )
    {
        return buildJWT( secret, true );
    }

    /**
     * Build a JWT String
     * 
     * @return The JWT String
     */
    private String buildJWT( String secret, boolean doSign )
    {
        JwtBuilder builder = Jwts.builder( );

        long lNow = new Date( ).getTime( );
        Date dateIssueAt = new Date( lNow );
        Date dateExpiration = new Date( lNow + 300000L );

        // builder.setIssuedAt( dateIssueAt );
        // builder.setExpiration( dateExpiration );
        Map<String, Object> mapClaims = new HashMap<String, Object>( );
        mapClaims.put( Constants.CLAIM_NONCE, NONCE );
        mapClaims.put( Constants.CLAIM_IDP, IDP );
        mapClaims.put( Constants.CLAIM_ACR, ACR );
        mapClaims.put( "exp", dateExpiration );
        mapClaims.put( "iat", dateIssueAt );
        mapClaims.put( "sub", SUBJECT );
        mapClaims.put( "aud", AUDIENCE );
        mapClaims.put( "iss", ISSUER );

        builder.claims().empty().add(mapClaims).and();

        if ( doSign )
        {
            builder.signWith( Keys.hmacShaKeyFor( secret.getBytes( StandardCharsets.UTF_8 ) ), Jwts.SIG.HS512 );
        }

        return builder.compact( );
    }
}
