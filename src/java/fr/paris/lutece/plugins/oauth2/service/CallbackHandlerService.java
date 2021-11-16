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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import fr.paris.lutece.plugins.oauth2.web.CallbackHandler;
import fr.paris.lutece.plugins.oauth2.web.Constants;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.url.UrlItem;

/**
 * 
 * CallbackHandlerService
 *
 */
public class CallbackHandlerService
{

    private static CallbackHandlerService _singleton;
    private static Logger _logger = Logger.getLogger( Constants.LOGGER_OAUTH2 );

    /** Private constructor */
    private CallbackHandlerService( )
    {
    }

    /**
     * Return the unique instance
     * 
     * @return The unique instance
     */
    public static synchronized CallbackHandlerService instance( )
    {
        if ( _singleton == null )
        {
            _singleton = new CallbackHandlerService( );
        }

        return _singleton;
    }

    /**
     * Return Handler by name return the default handler if no name match
     * 
     * @param name
     *            the handler name
     * @return CallbackHandler
     */
    public CallbackHandler getCallbackHandler( String name )
    {

        CallbackHandler callbackHandler = null;

        List<CallbackHandler> callBackList = SpringContextService.getBeansOfType( CallbackHandler.class );

        if ( !StringUtils.isEmpty( name ) && callBackList.size( ) > 0 )
        {

            callbackHandler = callBackList.stream( ).filter( x -> name.equals( x.getHandlerName( ) ) ).findFirst( ).orElse( null );

        }

        // getDefaultHandler
        if ( callbackHandler == null )
        {

            callbackHandler = callBackList.stream( ).filter( x -> x.isDefault( ) ).findFirst( ).orElse( callBackList.stream( ).findFirst( ).orElse( null ) );

        }

        return callbackHandler;

    }

}
