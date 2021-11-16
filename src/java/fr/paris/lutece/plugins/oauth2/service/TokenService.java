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
package fr.paris.lutece.plugins.oauth2.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import fr.paris.lutece.plugins.oauth2.business.AuthClientConf;
import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.business.Token;
import fr.paris.lutece.plugins.oauth2.jwt.JWTParser;
import fr.paris.lutece.plugins.oauth2.jwt.TokenValidationException;
import fr.paris.lutece.plugins.oauth2.web.Constants;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

/**
 * TokenService
 */
public final class TokenService
{
    AuthClientConf _defaultClientConfig;
    AuthServerConf _defaultauthServerConfig;

    private static Logger _logger = Logger.getLogger( Constants.LOGGER_OAUTH2 );

    private static final String BEAN_AUTH_CLIENT_CONF = "oauth2.client";
    private static final String BEAN_AUTH_SERVER_CONF = "oauth2.server";

    private static TokenService _instance;

    /**
     * private constructor
     */
    private TokenService( )
    {
    }

    /**
     * private constructor
     */
    private TokenService( AuthClientConf defaultClientConfig, AuthServerConf defaultauthServerConfig )
    {
        _defaultClientConfig = defaultClientConfig;
        _defaultauthServerConfig = defaultauthServerConfig;
    }

    /**
     * Retieve a token using an authorization code
     * 
     * @param strAuthorizationCode
     *            The authorization code
     * @param session
     *            The HTTP session
     * @return The token
     * @throws IOException
     *             if an error occurs
     * @throws HttpAccessException
     *             if an error occurs
     * @throws TokenValidationException
     *             If the token validation failed
     */
    public Token getToken( String strAuthorizationCode, HttpSession session, JWTParser jWTParser, String strStoredNonce )
            throws IOException, HttpAccessException, TokenValidationException
    {

        return getToken( null, _instance._defaultClientConfig, _instance._defaultauthServerConfig, strAuthorizationCode, session, jWTParser, strStoredNonce );
    }

    /**
     * Retieve a token using an authorization code
     * 
     * @param the
     *            strRedirectUri
     * @param strAuthorizationCode
     *            The authorization code
     * @param session
     *            The HTTP session
     * @return The token
     * @throws IOException
     *             if an error occurs
     * @throws HttpAccessException
     *             if an error occurs
     * @throws TokenValidationException
     *             If the token validation failed
     */
    public Token getToken( String strRedirectUri, AuthClientConf clientConfig, AuthServerConf authServerConf, String strAuthorizationCode, HttpSession session,
            JWTParser jWTParser, String strStoredNonce ) throws IOException, HttpAccessException, TokenValidationException
    {

        Token token = null;
        if ( strRedirectUri == null )
        {
            strRedirectUri = clientConfig.getRedirectUri( );
        }
        Map<String, String> mapParameters = new ConcurrentHashMap<String, String>( );
        mapParameters.put( Constants.PARAMETER_GRANT_TYPE, Constants.GRANT_TYPE_AUTHORIZATION_CODE );
        mapParameters.put( Constants.PARAMETER_CODE, strAuthorizationCode );
        mapParameters.put( Constants.PARAMETER_CLIENT_ID, clientConfig.getClientId( ) );
        mapParameters.put( Constants.PARAMETER_CLIENT_SECRET, clientConfig.getClientSecret( ) );

        if ( strRedirectUri != null )
        {
            mapParameters.put( Constants.PARAMETER_REDIRECT_URI, strRedirectUri );
        }

        HttpAccess httpAccess = new HttpAccess( );
        String strUrl = authServerConf.getTokenEndpointUri( );

        _logger.debug( "Posted URL : " + strUrl + "\nParameters :\n" + OauthUtils.traceMap( mapParameters ) );

        String strResponse = httpAccess.doPost( strUrl, mapParameters );
        _logger.debug( "Oauth2 response : " + strResponse );

        if ( !StringUtils.isEmpty( strResponse ) )
        {
            token = TokenService.getService( ).parse( strResponse, clientConfig, authServerConf, jWTParser, strStoredNonce );
        }
        return token;
    }

    /**
     *
     * Validate refresh token
     * 
     * @param clientConfig
     *            ClientConf
     * @param authServerConf
     *            AutConf
     * @param strRefreshToken
     *            refreshToken
     * @return true if the refresh token is already good
     *
     */
    public boolean validateRefreshToken( String strRefreshToken )
    {

        return validateRefreshToken( _instance._defaultClientConfig, _instance._defaultauthServerConfig, strRefreshToken );
    }

    /**
     *
     * Validate refresh token
     * 
     * @param clientConfig
     *            ClientConf
     * @param authServerConf
     *            AutConf
     * @param strRefreshToken
     *            refreshToken
     * @return true if the refresh token is already good
     *
     */
    public boolean validateRefreshToken( AuthClientConf clientConfig, AuthServerConf authServerConf, String strRefreshToken )
    {

        Map<String, String> mapParameters = new ConcurrentHashMap<String, String>( );
        Map<String, String> mapResponseHeader = new ConcurrentHashMap<String, String>( );
        mapParameters.put( Constants.PARAMETER_GRANT_TYPE, Constants.GRANT_TYPE_REFRESH_TOKEN );
        mapParameters.put( Constants.PARAMETER_REFRESH_TOKEN, strRefreshToken );
        mapParameters.put( Constants.PARAMETER_CLIENT_ID, clientConfig.getClientId( ) );
        mapParameters.put( Constants.PARAMETER_CLIENT_SECRET, clientConfig.getClientSecret( ) );
        HttpAccess httpAccess = new HttpAccess( );
        String strUrl = authServerConf.getTokenEndpointUri( );

        _logger.debug( "Validate Refresh Token : call URL  " + strUrl + "\nParameters :\n" + OauthUtils.traceMap( mapParameters ) );

        try
        {
            String strResponse = httpAccess.doPost( strUrl, mapParameters, null, null, mapResponseHeader );
            if ( !strResponse.contains( "\"error\"" ) )
            {
                return true;
            }
        }
        catch( HttpAccessException e )
        {

        }

        return false;
    }

    /**
     *
     * Get new Token using refresh token
     * 
     * @param clientConfig
     *            ClientConf
     * @param authServerConf
     *            AutConf
     * @param strRefreshToken
     *            refreshToken
     * @return true if the refresh token is already good
     *
     */
    public Token getTokenByRefreshToken( String strRefreshToken )
    {
        return getTokenByRefreshToken( _instance._defaultClientConfig, _instance._defaultauthServerConfig, strRefreshToken );
    }

    /**
     *
     * Get new Token using refresh token
     * 
     * @param clientConfig
     *            ClientConf
     * @param authServerConf
     *            AutConf
     * @param strRefreshToken
     *            refreshToken
     * @return true if the refresh token is already good
     *
     */
    public Token getTokenByRefreshToken( AuthClientConf clientConfig, AuthServerConf authServerConf, String strRefreshToken )
    {

        Map<String, String> mapParameters = new ConcurrentHashMap<String, String>( );
        Map<String, String> mapResponseHeader = new ConcurrentHashMap<String, String>( );
        mapParameters.put( Constants.PARAMETER_GRANT_TYPE, Constants.GRANT_TYPE_REFRESH_TOKEN );
        mapParameters.put( Constants.PARAMETER_REFRESH_TOKEN, strRefreshToken );
        mapParameters.put( Constants.PARAMETER_CLIENT_ID, clientConfig.getClientId( ) );
        mapParameters.put( Constants.PARAMETER_CLIENT_SECRET, clientConfig.getClientSecret( ) );
        Token newToken = null;
        HttpAccess httpAccess = new HttpAccess( );
        String strUrl = authServerConf.getTokenEndpointUri( );

        _logger.debug( "Get Token By Refresh Token : call URL  " + strUrl + "\nParameters :\n" + OauthUtils.traceMap( mapParameters ) );

        try
        {
            String strResponse = httpAccess.doPost( strUrl, mapParameters, null, null, mapResponseHeader );
            if ( !StringUtils.isEmpty( strResponse ) && !strResponse.contains( "\"error\"" ) )
            {
                newToken = TokenService.getService( ).parse( strResponse, clientConfig, authServerConf, null, null );
            }
        }
        catch( IOException e )
        {
            // TODO Auto-generated catch block
            _logger.error( "Error getting new Token using refresh token", e );
        }
        catch( TokenValidationException e )
        {
            // TODO Auto-generated catch block
            _logger.error( "Error getting new Token using refresh token", e );
        }
        catch( HttpAccessException e )
        {

        }

        return newToken;
    }

    /**
     * parse the JSON for a token
     *
     * @param strJson
     *            The JSON
     * @param clientConfig
     *            The client configuration
     * @param serverConfig
     *            The server configuration
     * @param strStoredNonce
     *            The stored nonce
     * @return The Token
     * @throws java.io.IOException
     *             if an error occurs
     * @throws TokenValidationException
     *             If the token validation failed
     */
    public Token parse( String strJson, AuthClientConf clientConfig, AuthServerConf serverConfig, JWTParser jwtParser, String strStoredNonce )
            throws IOException, TokenValidationException
    {
        Token token = parseToken( strJson );

        _logger.debug( token );

        if ( jwtParser != null && serverConfig.isEnableJwtParser( ) )
        {
            jwtParser.parseJWT( token, clientConfig, serverConfig, strStoredNonce, _logger );
        }
        return token;
    }

    /**
     * Parse the Token from a JSON string
     * 
     * @param strJson
     *            The JSON string
     * @return The Token
     * @throws IOException
     *             if an error occurs
     */
    Token parseToken( String strJson ) throws IOException
    {
        return MapperService.parse( strJson, Token.class );
    }

    public static TokenService getService( )
    {
        if ( _instance == null )
        {

            _instance = new TokenService( SpringContextService.getBean( BEAN_AUTH_CLIENT_CONF ), SpringContextService.getBean( BEAN_AUTH_SERVER_CONF ) );
        }
        return _instance;

    }

}
