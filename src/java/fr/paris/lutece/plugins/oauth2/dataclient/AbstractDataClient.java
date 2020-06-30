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
package fr.paris.lutece.plugins.oauth2.dataclient;

import fr.paris.lutece.plugins.oauth2.business.Token;
import fr.paris.lutece.plugins.oauth2.service.BearerTokenAuthenticator;
import fr.paris.lutece.plugins.oauth2.web.Constants;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.signrequest.RequestAuthenticator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DataClient
 */
public abstract class AbstractDataClient implements DataClient
{
    protected static Logger _logger = Logger.getLogger( Constants.LOGGER_OAUTH2 );

    private static final char SEPARATOR = '+';

    private String _strName;
    private String _strRedirectUri;
    private String _strDataServerUri;
    private String _strTokenMethod;
    private Set<String> _scope;
    private Set<String> _acrValues;
    private boolean _bDefault;

    /**
     * {@inheritDoc }
     */
    @Override
    public String getName( )
    {
        return _strName;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Set getScope( )
    {
        return _scope;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setScope( Set scope )
    {
        _scope = scope;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getScopes( )
    {
        StringBuilder sbScopes = new StringBuilder( );

        Iterator iterator = _scope.iterator( );
        boolean bFirst = true;

        while ( iterator.hasNext( ) )
        {
            if ( !bFirst )
            {
                sbScopes.append( SEPARATOR );
            }

            bFirst = false;
            sbScopes.append( iterator.next( ) );
        }

        return sbScopes.toString( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getRedirectUri( )
    {
        return _strRedirectUri;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setRedirectUri( String strRedirectUri )
    {
        _strRedirectUri = strRedirectUri;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Set getAcrValuesSet( )
    {
        return _acrValues;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setAcrValuesSet( Set acrValues )
    {
        _acrValues = acrValues;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getAcrValues( )
    {
        if ( _acrValues == null || _acrValues.isEmpty( ) )
        {
            return null;
        }

        StringBuilder sbAcrValues = new StringBuilder( );

        Iterator iterator = _acrValues.iterator( );
        boolean bFirst = true;

        while ( iterator.hasNext( ) )
        {
            if ( !bFirst )
            {
                sbAcrValues.append( SEPARATOR );
            }

            bFirst = false;
            sbAcrValues.append( iterator.next( ) );
        }

        return sbAcrValues.toString( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getDataServerUri( )
    {
        return _strDataServerUri;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setDataServerUri( String strDataServerUri )
    {
        _strDataServerUri = strDataServerUri;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getTokenMethod( )
    {
        return _strTokenMethod;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setTokenMethod( String strTokenMethod )
    {
        _strTokenMethod = strTokenMethod;
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

        String strUrl = _strDataServerUri;

        try
        {
            RequestAuthenticator authenticator = new BearerTokenAuthenticator( token.getAccessToken( ) );
            strResponse = httpAccess.doGet( strUrl, authenticator, null );
            _logger.debug( "Oauth2 response : " + strResponse );
        }
        catch( HttpAccessException ex )
        {
            _logger.error( "OAuth Login Error" + ex.getMessage( ), ex );
        }

        return strResponse;
    }

    public void handleError( HttpServletRequest request, HttpServletResponse response, String strError )
    {
        try
        {
            UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + AppPropertiesService.getProperty( Constants.PROPERTY_ERROR_PAGE ) );
            url.addParameter( Constants.PARAMETER_ERROR, strError );
            _logger.info( strError );
            response.sendRedirect( url.getUrl( ) );
        }
        catch( IOException ex )
        {
            _logger.error( "Error redirecting to the error page : " + ex.getMessage( ), ex );
        }
    }

    public boolean isDefault( )
    {
        return _bDefault;
    }

    public void setDefault( boolean _bDefault )
    {
        this._bDefault = _bDefault;
    }

}
