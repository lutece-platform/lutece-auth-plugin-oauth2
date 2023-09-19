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
package fr.paris.lutece.plugins.oauth2.web;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import fr.paris.lutece.plugins.oauth2.business.AuthClientConf;
import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.business.Token;
import fr.paris.lutece.plugins.oauth2.dataclient.DataClient;
import fr.paris.lutece.plugins.oauth2.jwt.JWTParser;
import fr.paris.lutece.plugins.oauth2.jwt.TokenValidationException;
import fr.paris.lutece.plugins.oauth2.service.DataClientService;
import fr.paris.lutece.plugins.oauth2.service.PkceUtil;
import fr.paris.lutece.plugins.oauth2.service.TokenService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.url.UrlItem;

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
            url.addParameter( Constants.PARAMETER_REDIRECT_URI, URLEncoder.encode( generateRedirectUrl( request, dataClient ), "UTF-8" ) );
            url.addParameter( Constants.PARAMETER_SCOPE, dataClient.getScopes( ) );
            url.addParameter( Constants.PARAMETER_STATE, createState( session ) );
            url.addParameter( Constants.PARAMETER_NONCE, createNonce( session ) );
            if(_authClientConf.isPkce())
            {
            	String strCodeVerifier=createCodeVerifier(session);
            	String strCodeChallenge=createCodeChallenge(strCodeVerifier, session);
            	url.addParameter(Constants.PARAMETER_CODE_CHALLENGE,strCodeChallenge);
            	url.addParameter(Constants.PARAMETER_CODE_CHALLENGE_METHOD,"S256");
            }
            
            
            

            addComplementaryParameters( url, request );
            addBackPromptUrl(url, request);
            
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
            handleError( request, response, Constants.ERROR_TYPE_RETRIEVING_AUTHORIZATION_CODE );
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
            handleError( request, response, Constants.ERROR_TYPE_INVALID_STATE );

            return;
        }

        try
        {
            HttpSession session = request.getSession( );
            DataClient dataClient = DataClientService.instance( ).getClient( request );
            String strRedirectUri = generateRedirectUrl( request, dataClient );
            Token token = getToken( strRedirectUri, strCode, session );
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
     * @param the
     *            client redirect Uri
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
    private Token getToken( String strRedirectUri, String strAuthorizationCode, HttpSession session )
            throws IOException, HttpAccessException, TokenValidationException
    {

        return TokenService.getService( ).getToken( strRedirectUri, _authClientConf, _authServerConf, strAuthorizationCode, session, _jWTParser,
                getStoredNonce( session ),getStoredCodeVerifier(session) );

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
    private String createNonce( HttpSession session )
    {
        String nonce = new BigInteger( 128, new SecureRandom( ) ).toString( 16 );
        session.setAttribute( getNonceAttributeSessionName( ), nonce );

        return nonce;
    }
    
    
    /**
     * Create a cryptographically random nonce and store it in the session
     *
     * @param session
     *            The session
     * @return The nonce
     */
    private String createCodeChallenge(String strCodeVerifier,HttpSession session )
    {
        
    	String strChallenge=null;
		try {
			if(strCodeVerifier!=null)
			{
				strChallenge = PkceUtil.generateCodeChallenge(strCodeVerifier);
			}
		} catch (UnsupportedEncodingException|NoSuchAlgorithmException  e) {
			AppLogService.error(e);
		}
    			
        session.setAttribute( getCodeChallengeAttributeSessionName(), strChallenge );

        return strChallenge;
    }
    
    /**
     * Create a cryptographically random nonce and store it in the session
     *
     * @param session
     *            The session
     * @return The nonce
     */
    private String createCodeVerifier( HttpSession session )
    {
        String StrCodeVerifier=null;
		try {
			StrCodeVerifier = PkceUtil.generateCodeVerifier();
		} catch (UnsupportedEncodingException e) {
			AppLogService.error(e);
		}
        session.setAttribute( getCodeVerifierAttributeSessionName(), StrCodeVerifier );

        return StrCodeVerifier;
    }
    
    

    /**
     * Get the store code verifier 
     *
     * @param session
     *            The session
     * @return The stored nonce
     */
    private String getStoredCodeVerifier( HttpSession session )
    {
        return getStoredSessionString( session, getCodeVerifierAttributeSessionName() );
    }
    
    
    /**
     * Get the store code verifier 
     *
     * @param session
     *            The session
     * @return The stored nonce
     */
    private String getStoredCodeChallenge( HttpSession session )
    {
        return getStoredSessionString( session, getCodeChallengeAttributeSessionName() );
    }
    
    
    
    
    /**
     * Get the nonce we stored in the session
     *
     * @param session
     *            The session
     * @return The stored nonce
     */
    private String getStoredNonce( HttpSession session )
    {
        return getStoredSessionString( session, getNonceAttributeSessionName( ) );
    }
    
    

    /**
     * 
     * @return the attribute name who store nonced
     */
    private String getNonceAttributeSessionName( )
    {

        return getHandlerName( ) == null ? Constants.NONCE_SESSION_VARIABLE : getHandlerName( ) + Constants.NONCE_SESSION_VARIABLE;
    }
    
    /**
     * 
     * @return the attribute name who store the verifier code
     */
    private String getCodeVerifierAttributeSessionName( )
    {

        return getHandlerName( ) == null ? Constants.CODE_VERIFIER_SESSION_VARIABLE : getHandlerName( ) + Constants.CODE_VERIFIER_SESSION_VARIABLE;
    }
    
    
    /**
     * 
     * @return the attribute name who store the challenge attribute
     */
    private String getCodeChallengeAttributeSessionName( )
    {

        return getHandlerName( ) == null ? Constants.CODE_CHALLENGE_SESSION_VARIABLE : getHandlerName( ) + Constants.CODE_CHALLENGE_SESSION_VARIABLE;
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
    private String createState( HttpSession session )
    {
        String strState = new BigInteger( 128, new SecureRandom( ) ).toString( 16 );
        session.setAttribute( getStateAttributeSessionName( ), strState );

        return strState;
    }

    /**
     * 
     * @return the attribute name who store the sate
     */
    private String getStateAttributeSessionName( )
    {

        return getHandlerName( ) == null ? Constants.STATE_SESSION_VARIABLE : getHandlerName( ) + Constants.STATE_SESSION_VARIABLE;
    }

    /**
     * Get the state we stored in the session
     *
     * @param session
     *            The session
     * @return The stored state
     */
    private String getStoredState( HttpSession session )
    {
        return getStoredSessionString( session, getStateAttributeSessionName( ) );
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
        String[] strComplementaryParams = request.getParameterValues( Constants.PARAMETER_COMPLEMENTARY_PARAMETER );
        String strParamValue;
        String strParamCode;
       
        if ( strComplementaryParams!=null && strComplementaryParams.length > 0  )
        {
        	for (int i = 0; i < strComplementaryParams.length; i++) {
        	
        		if(strComplementaryParams[i].contains( "=" )) 
        		{
	        		 strParamCode = strComplementaryParams[i].split( "=" ) [0];
	                 strParamValue = strComplementaryParams[i].substring( strComplementaryParams[i].indexOf( "=" ) + 1, strComplementaryParams[i].length( ) );
	                 try
	                 {
	                     strParamValue = URLEncoder.encode( strParamValue, "UTF-8" );
	                 }
	                 catch( UnsupportedEncodingException e )
	                 {
	                     _logger.error( "error during urlEncode of param" + strParamValue, e );
	                 }
	                 url.addParameter( strParamCode, strParamValue );
        		}
        		
        	}
        	
        }

    }
    
    
    
    private void addBackPromptUrl( UrlItem url, HttpServletRequest request )
    {
        String strBackPromptUrl= request.getParameter( Constants.PARAMETER_BACK_PROMPT_URL);
  
        if ( !StringUtils.isEmpty( strBackPromptUrl) )
        {
            try
            {
            	
            	  url.addParameter( Constants.PARAMETER_BACK_PROMPT_URL, URLEncoder.encode( strBackPromptUrl, "UTF-8" ) );
        
            }
            catch( UnsupportedEncodingException e )
            {
                _logger.error( "error during urlEncode of param " +Constants.PARAMETER_BACK_PROMPT_URL  + strBackPromptUrl, e );
            }
          
        }

    }
    

    /**
     * Generate Redirect Url
     * 
     * @param request
     *            the httpServletRequest
     * @param dataClient
     *            the dataCleint
     * @return the
     * 
     */
    private String generateRedirectUrl( HttpServletRequest request, DataClient dataClient )
    {
        String stRedirectUrl = _authClientConf.getRedirectUri( );
        UrlItem url = new UrlItem(stRedirectUrl);
        
        if ( stRedirectUrl == null )
        {
            stRedirectUrl = DataClientService.instance( ).getDataClientUrl( request, dataClient.getName( ), getHandlerName( ) );
            url=new UrlItem(stRedirectUrl);
        
        }
        else
        {
        	
            // add dataclient and handler name parameter
           url.addParameter(Constants.PARAMETER_DATA_CLIENT, dataClient.getName( ));
           if ( getHandlerName( ) != null )
            {
        	   url.addParameter(Constants.PARAMETER_HANDLER_NAME,  getHandlerName( ));
                
            }
        }
        
        String strBackPromptUrl= request.getParameter( Constants.PARAMETER_BACK_PROMPT_URL);
        
        if ( !StringUtils.isEmpty( strBackPromptUrl) )
        {
        	
			try {
				
				  url.addParameter(Constants.PARAMETER_BACK_PROMPT_URL,URLEncoder.encode(strBackPromptUrl, "UTF-8"));
			
			} catch (UnsupportedEncodingException e) {
				_logger.error(
						"error during urlEncode of param " + Constants.PARAMETER_BACK_PROMPT_URL + strBackPromptUrl, e);
			}
        	
        }
        
        return url.getUrl();
    }

}
