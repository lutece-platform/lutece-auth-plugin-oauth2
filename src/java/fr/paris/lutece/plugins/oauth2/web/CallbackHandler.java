/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.oauth2.web;

import fr.paris.lutece.plugins.oauth2.business.AuthClientConf;
import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.business.Token;
import fr.paris.lutece.plugins.oauth2.dataclient.DataClient;
import fr.paris.lutece.plugins.oauth2.jwt.JWTParser;
import fr.paris.lutece.plugins.oauth2.jwt.TokenValidationException;
import fr.paris.lutece.plugins.oauth2.service.DataClientService;
import fr.paris.lutece.plugins.oauth2.service.TokenService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;

import java.net.URLEncoder;

import java.security.SecureRandom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * CallbackHandler
 */
public class CallbackHandler implements Serializable
{
    private static final String PROPERTY_ERROR_PAGE = "oauth2.error.page";
    private static final long serialVersionUID = 1L;
    private static Logger _logger = Logger.getLogger( Constants.LOGGER_OAUTH2 );
    private String _handlerName;
    private AuthServerConf _authServerConf;
    private AuthClientConf _authClientConf;
    private JWTParser _jWTParser;
    private boolean _bDefault;

    /**
     * @return the authServerConf
     */
    public AuthServerConf getAuthServerConf( )
    {
        return _authServerConf;
    }

    /**
     * @param authServerConf
     *            the authServerConf to set
     */
    public void setAuthServerConf( AuthServerConf authServerConf )
    {
        _authServerConf = authServerConf;
    }

    /**
     * @return the authClientConf
     */
    public AuthClientConf getAuthClientConf( )
    {
        return _authClientConf;
    }

    /**
     * @param authClientConf
     *            the authClientConf to set
     */
    public void setAuthClientConf( AuthClientConf authClientConf )
    {
        _authClientConf = authClientConf;
    }

    /**
     * Handle the callback
     * 
     * @param request
     *            The HTTP request
     * @param response
     *            The HTTP response
     */
    void handle( HttpServletRequest request, HttpServletResponse response )
    {
        String strError = request.getParameter( Constants.PARAMETER_ERROR );
        String strCode = request.getParameter( Constants.PARAMETER_CODE );

        if ( strError != null )
        {
            handleError( request, response, strError );
        }
        else
            if ( strCode != null )
            {
                handleAuthorizationCodeResponse( request, response );
            }
            else
            {
                handleAuthorizationRequest( request, response );
            }
    }

    /**
     * Handle an error
     *
     * @param request
     *            The HTTP request
     * @param response
     *            The HTTP response
     * @param strError
     *            The Error message
     */
    private void handleError( HttpServletRequest request, HttpServletResponse response, String strError )
    {
        DataClient dataClient = DataClientService.instance( ).getClient( request );

        if ( dataClient != null )
        {
            // handle error of the data client
            dataClient.handleError( request, response, strError );
        }
        else
        {
            // default method if there is no dataclient
            try
            {
                UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + AppPropertiesService.getProperty( PROPERTY_ERROR_PAGE ) );
                url.addParameter( Constants.PARAMETER_ERROR, strError );
                _logger.info( strError );
                response.sendRedirect( url.getUrl( ) );
            }
            catch( IOException ex )
            {

                _logger.error( "Error redirecting to the error page : " + ex.getMessage( ), ex );
            }
        }
    }

    /**
     * Handle an authorization request to obtain an authorization code
     * 
     * @param request
     *            The HTTP request
     * @param response
     *            The HTTP response
     */
    private void handleAuthorizationRequest( HttpServletRequest request, HttpServletResponse response )
    {
        try
        {
            HttpSession session = request.getSession( true );

           
            DataClient dataClient = DataClientService.instance( ).getClient( request );

            UrlItem url = new UrlItem( _authServerConf.getAuthorizationEndpointUri( ) );
            url.addParameter( Constants.PARAMETER_CLIENT_ID, _authClientConf.getClientId( ) );
            url.addParameter( Constants.PARAMETER_RESPONSE_TYPE, Constants.RESPONSE_TYPE_CODE );
            url.addParameter( Constants.PARAMETER_REDIRECT_URI, URLEncoder.encode( _authClientConf.getRedirectUri( ), "UTF-8" ) );
            url.addParameter( Constants.PARAMETER_SCOPE, dataClient.getScopes( ) );
            url.addParameter( Constants.PARAMETER_STATE, createState( session ) );
            url.addParameter( Constants.PARAMETER_NONCE, createNonce( session ) );
            addComplementaryParameters( url, request );
            String strAcrValues = dataClient.getAcrValues( );
            if ( strAcrValues != null )
            {
                url.addParameter( Constants.PARAMETER_ACR_VALUES, strAcrValues );
            }

            String strUrl = url.getUrl( );
            _logger.debug( "OAuth request : " + strUrl );
            response.sendRedirect( strUrl );
        }
        catch( IOException ex )
        {
            String strError = "Error retrieving an authorization code : " + ex.getMessage( );
            _logger.error( strError, ex );
            handleError( request, response, strError );
        }
    }

    /**
     * Handle an request that contains an authorization code
     *
     * @param request
     *            The HTTP request
     * @param response
     *            The HTTP response
     */
    private void handleAuthorizationCodeResponse( HttpServletRequest request, HttpServletResponse response )
    {
        String strCode = request.getParameter( Constants.PARAMETER_CODE );

        _logger.info( "OAuth Authorization code received : " + strCode );

        // Check valid state
        if ( !checkState( request ) )
        {
            handleError( request, response, "Invalid state returned Oauth server !" );

            return;
        }

        try
        {
            HttpSession session = request.getSession( );
            Token token = getToken( strCode, session );
            DataClient dataClient = DataClientService.instance( ).getClient( request );
            dataClient.handleToken( token, request, response );
        }
        catch( IOException ex )
        {
            String strError = "Error retrieving token : " + ex.getMessage( );
            _logger.error( strError, ex );
            handleError( request, response, strError );
        }
        catch( HttpAccessException ex )
        {
            String strError = "Error retrieving token : " + ex.getMessage( );
            _logger.error( strError, ex );
            handleError( request, response, strError );
        }
        catch( TokenValidationException ex )
        {
            String strError = "Error retrieving token : " + ex.getMessage( );
            _logger.error( strError, ex );
            handleError( request, response, strError );
        }
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
    private Token getToken( String strAuthorizationCode, HttpSession session ) throws IOException, HttpAccessException, TokenValidationException
    {

        return TokenService.getService( ).getToken( _authClientConf, _authServerConf, strAuthorizationCode, session, _jWTParser, getStoredNonce( session ) );

    }

    ////////////////////////////////////////////////////////////////////////////
    // Check and trace utils

    /**
     * Create a cryptographically random nonce and store it in the session
     *
     * @param session
     *            The session
     * @return The nonce
     */
    private static String createNonce( HttpSession session )
    {
        String nonce = new BigInteger( 50, new SecureRandom( ) ).toString( 16 );
        session.setAttribute( Constants.NONCE_SESSION_VARIABLE, nonce );

        return nonce;
    }

    /**
     * Get the nonce we stored in the session
     *
     * @param session
     *            The session
     * @return The stored nonce
     */
    private static String getStoredNonce( HttpSession session )
    {
        return getStoredSessionString( session, Constants.NONCE_SESSION_VARIABLE );
    }

    /**
     * check state returned by Oauth2 to the callback uri
     * 
     * @param request
     *            The HTTP request
     * @return True if the state is valid
     */
    private boolean checkState( HttpServletRequest request )
    {
        String strState = request.getParameter( Constants.PARAMETER_STATE );
        HttpSession session = request.getSession( );
        String strStored = getStoredState( session );
        boolean bCheck = ( ( strState == null ) || strState.equals( strStored ) );

        if ( !bCheck )
        {
            _logger.debug( "Bad state returned by server : " + strState + " while expecting : " + strStored );
        }

        return bCheck;
    }

    /**
     * Create a cryptographically random state and store it in the session
     *
     * @param session
     *            The session
     * @return The state
     */
    private static String createState( HttpSession session )
    {
        String strState = new BigInteger( 50, new SecureRandom( ) ).toString( 16 );
        session.setAttribute( Constants.STATE_SESSION_VARIABLE, strState );

        return strState;
    }

    /**
     * Get the state we stored in the session
     *
     * @param session
     *            The session
     * @return The stored state
     */
    private static String getStoredState( HttpSession session )
    {
        return getStoredSessionString( session, Constants.STATE_SESSION_VARIABLE );
    }

    /**
     * Get the named stored session variable as a string. Return null if not found or not a string.
     *
     * @param session
     *            The session
     * @param strKey
     *            The key
     * @return The session string
     */
    private static String getStoredSessionString( HttpSession session, String strKey )
    {
        Object object = session.getAttribute( strKey );

        if ( ( object != null ) && object instanceof String )
        {
            return object.toString( );
        }
        else
        {
            return null;
        }
    }

    /**
     * get the handler Name
     * 
     * @return the handler name
     */
    public String getHandlerName( )
    {
        return _handlerName;
    }

    /**
     * set the handler name
     * 
     * @param _handlerName
     *            specify the handler Name
     */
    public void setHandlerName( String _handlerName )
    {
        this._handlerName = _handlerName;
    }

    /**
     * 
     * @return JWTParser
     */
    public JWTParser getJWTParser( )
    {
        return _jWTParser;
    }

    /**
     * 
     * @param jWTParser
     *            set JwtParser
     */
    public void setJWTParser( JWTParser jWTParser )
    {
        this._jWTParser = jWTParser;
    }

    /**
     * 
     * @return true if the handler is the default handler
     */
    public boolean isDefault( )
    {
        return _bDefault;
    }

    /**
     * 
     * @param _bDefault
     *            true if the handler is the default handler
     */
    public void setDefault( boolean _bDefault )
    {
        this._bDefault = _bDefault;
    }

    private void addComplementaryParameters( UrlItem url, HttpServletRequest request )
    {
        String strComplementaryParam = request.getParameter( Constants.PARAMETER_COMPLEMENTARY_PARAMETER );
        if(!StringUtils.isEmpty( strComplementaryParam))
        {
            String [ ] tabComplementaryParameters = strComplementaryParam.split( "&" );
            for ( int i = 0; i < tabComplementaryParameters.length; i++ )
            {
                if ( tabComplementaryParameters [i].contains( "=" ) )
                {
                    url.addParameter( tabComplementaryParameters [i].split( "=" ) [0], tabComplementaryParameters [i].split( "=" ) [1] );
                }
            }
        }

    }
}
