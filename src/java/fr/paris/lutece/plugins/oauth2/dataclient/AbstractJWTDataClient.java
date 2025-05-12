/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.plugins.oauth2.dataclient;



import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.oauth2.business.AuthClientConf;
import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.business.Token;
import fr.paris.lutece.plugins.oauth2.jwt.JWTParser;
import fr.paris.lutece.plugins.oauth2.jwt.TokenValidationException;
import fr.paris.lutece.plugins.oauth2.service.BearerTokenAuthenticator;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.signrequest.RequestAuthenticator;

/**
 * 
 * AbstractJWTDataClient
 *
 */
public abstract class AbstractJWTDataClient extends AbstractDataClient
{

    private AuthServerConf _authServerConf;
    private AuthClientConf _authClientConf;
    private JWTParser _jWTParser;
    
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
     * Send an authenticated request with the access token to retreive data
     * 
     * @param token
     *            The token
     * @return The response
     */
    public String getData( Token token )
    {
        String strResponse = null;
        HttpAccess httpAccess = new HttpAccess( );

        String strUrl = getDataServerUri( );

        try
        {
            RequestAuthenticator authenticator = new BearerTokenAuthenticator( token.getAccessToken( ) );
            strResponse = httpAccess.doGet( strUrl, authenticator, null );
            
            if ( StringUtils.isNotEmpty( strResponse )  )
            {                      
                strResponse = _jWTParser.parseJWT( strResponse, _authClientConf, _authServerConf, _logger );
            }
            
            _logger.debug( "Oauth2 response : " + strResponse );
        }
        catch( HttpAccessException | TokenValidationException ex )
        {
            _logger.error( "OAuth Login Error" + ex.getMessage( ), ex );
        }

        return strResponse;
    }

}
