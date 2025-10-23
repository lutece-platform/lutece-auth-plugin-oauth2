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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import fr.paris.lutece.plugins.oauth2.dataclient.DataClient;
import fr.paris.lutece.plugins.oauth2.web.Constants;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.url.UrlItem;

/**
 * DataClientService
 */
@ApplicationScoped
public class DataClientService
{
   
   @Inject  
   private Instance<DataClient>  _dataClients;


public DataClientService( )
    {
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
    private DataClient getClient( String strName )
    {
        return _dataClients.stream()
                .filter( dataClient -> StringUtils.equals ( dataClient.getName( ), strName ) )
                .findFirst( )
                .orElseThrow( () -> new IllegalArgumentException( "No DataClient found with name: " + strName ) );   
        
        
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

        return _dataClients.stream( ).filter( x -> x.isDefault( )).findFirst( )
                .orElse( _dataClients.stream(   ).findFirst( ).orElse( null ) );

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

}
