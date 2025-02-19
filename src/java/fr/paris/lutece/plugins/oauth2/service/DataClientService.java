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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import fr.paris.lutece.plugins.oauth2.business.AuthClientConf;
import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.dataclient.DataClient;
import fr.paris.lutece.plugins.oauth2.jwt.JWTParser;
import fr.paris.lutece.plugins.oauth2.web.Constants;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.url.UrlItem;

/**
 * DataClientService
 */
public final class DataClientService
{
    private static DataClientService _singleton;
    private static ConcurrentMap<String, DataClient> _mapClients;
    private static Logger _logger = Logger.getLogger( Constants.LOGGER_OAUTH2 );

    /** Private constructor */
    private DataClientService( )
    {
    }

    /**
     * Return the unique instance
     * 
     * @return The unique instance
     */
    public static synchronized DataClientService instance( )
    {
        if ( _singleton == null )
        {
            _singleton = new DataClientService( );
            initClients( );
        }

        return _singleton;
    }

    /**
     * Init clients
     */
    private static void initClients( )
    {
        _mapClients = new ConcurrentHashMap<>( );

        for ( DataClient client : SpringContextService.getBeansOfType( DataClient.class ) )
        {
            _mapClients.put( client.getName( ), client );
            _logger.info( "New Oaut2 Data Client registered : " + client.getName( ) );
        }
    }

    /**
     * Gets a DataClient object for a given name
     * 
     * @param strName
     *            The Data Client name
     * @return The Data Client
     */
    public DataClient getClient( String strName )
    {
        return _mapClients.get( strName );
    }

    /**
     * Gets a DataClient object for a given name
     * 
     * @param strName
     *            The Data Client name
     * @return The Data Client
     */
    public DataClient getClient( HttpServletRequest request )
    {

        HttpSession session = request.getSession( true );
        DataClient dataClient = null;
        String strDataClientName = request.getParameter( Constants.PARAMETER_DATA_CLIENT );
        if ( !StringUtils.isEmpty( strDataClientName ) )
        {
            dataClient = getClient( strDataClientName );
        }
        else
        {
            session = request.getSession( true );
            dataClient = (DataClient) session.getAttribute( Constants.SESSION_ATTRIBUTE_DATACLIENT );

        }
        if ( dataClient != null )
        {

            session.setAttribute( Constants.SESSION_ATTRIBUTE_DATACLIENT, dataClient );

        }
        else
        {

            // get Default data client
            dataClient = getDefaultClient( request );
        }

        return dataClient;
    }

    /**
     * Gets a DataClient object for a given name
     * 
     * @param strName
     *            The Data Client name
     * @return The Data Client
     */
    public DataClient getDefaultClient( HttpServletRequest request )
    {

        return _mapClients.entrySet( ).stream( ).filter( x -> x.getValue( ).isDefault( ) ).map( x -> x.getValue( ) ).findFirst( )
                .orElse( _mapClients.entrySet( ).stream( ).map( x -> x.getValue( ) ).findFirst( ).orElse( null ) );

    }

    /**
     * Gets the dataclient URL
     * 
     * @param request
     *            the httpservlet Request
     * @param strDataClientName
     *            The data client name
     * @param strHandlerName
     *            the HandlerName
     * @return The URL
     */
    public String getDataClientUrl( HttpServletRequest request, String strDataClientName, String strHandlerName )
    {
        String strCallBackUrlUrl = AppPathService.getAbsoluteUrl( request, Constants.CALL_BACK_SERVLET_URI );

        UrlItem url = new UrlItem( strCallBackUrlUrl );
        url.addParameter( Constants.PARAMETER_DATA_CLIENT, strDataClientName );
        if ( strHandlerName != null )
        {
            url.addParameter( Constants.PARAMETER_HANDLER_NAME, strHandlerName );
        }
        return url.getUrl( );
    }
    
    /**
     * Set data client config
     * @param dataClient
     * @param authServerConfig
     * @param authClientConf
     * @param jwtParser
     */
    public void setDataClientConfig (DataClient dataClient, AuthServerConf authServerConfig, AuthClientConf authClientConf, JWTParser jwtParser )
    {
        dataClient.setAuthServerConf( authServerConfig );
        dataClient.setAuthClientConf( authClientConf );
        dataClient.setJWTParser( jwtParser );
    }

}
