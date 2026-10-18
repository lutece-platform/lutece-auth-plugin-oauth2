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

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.oauth2.dataclient.DataClient;
import fr.paris.lutece.plugins.oauth2.service.DataClientService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.enterprise.inject.Vetoed;
import jakarta.servlet.http.HttpServletRequest;

/**
 * CallbackHandler Test: unknown data client
 */
public class CallbackHandlerTest extends LuteceTestCase
{
    private static final String UNKNOWN_DATA_CLIENT = "zzz";

    /**
     * Response that keeps the redirect URL (not a CDI bean)
     */
    @Vetoed
    private static class RedirectResponse extends MockHttpServletResponse
    {
        private String _strRedirectUrl;

        @Override
        public void sendRedirect( String strUrl ) throws IOException
        {
            _strRedirectUrl = strUrl;
        }
    }

    /**
     * Data client service without any data client (not a CDI bean)
     */
    @Vetoed
    private static class NoDataClientService extends DataClientService
    {
        @Override
        public boolean isDataClient( String strName )
        {
            return false;
        }

        @Override
        public DataClient getClient( HttpServletRequest request )
        {
            return null;
        }
    }

    private CallbackHandler getHandler( )
    {
        return new CallbackHandler( "test", null, null, null, true, new NoDataClientService( ), null );
    }

    @Test
    public void testHandleUnknownDataClient( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Constants.PARAMETER_DATA_CLIENT, UNKNOWN_DATA_CLIENT );
        RedirectResponse response = new RedirectResponse( );

        getHandler( ).handle( request, response );

        assertNotNull( response._strRedirectUrl );
        assertTrue( response._strRedirectUrl.contains( Constants.ERROR_TYPE_UNKNOWN_DATA_CLIENT ) );
    }

    @Test
    public void testLogoutUnknownDataClient( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Constants.PARAMETER_DATA_CLIENT, UNKNOWN_DATA_CLIENT );
        RedirectResponse response = new RedirectResponse( );

        getHandler( ).logout( request, response );

        assertNotNull( response._strRedirectUrl );
        assertTrue( response._strRedirectUrl.contains( Constants.ERROR_TYPE_UNKNOWN_DATA_CLIENT ) );
    }

    /**
     * An authorization code without state is refused (login CSRF): no token request, redirect to the error page
     */
    @Test
    public void testCodeWithoutState( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Constants.PARAMETER_CODE, "code" );
        RedirectResponse response = new RedirectResponse( );

        getHandler( ).handle( request, response );

        assertNotNull( response._strRedirectUrl );
        assertTrue( response._strRedirectUrl.contains( Constants.ERROR_TYPE_INVALID_STATE ) );
    }

    /**
     * An authorization code with a state that is not the one of the session is refused
     */
    @Test
    public void testCodeWithWrongState( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Constants.PARAMETER_CODE, "code" );
        request.addParameter( Constants.PARAMETER_STATE, "forged" );
        request.getSession( true ).setAttribute( "test" + Constants.STATE_SESSION_VARIABLE, "expected" );
        RedirectResponse response = new RedirectResponse( );

        getHandler( ).handle( request, response );

        assertNotNull( response._strRedirectUrl );
        assertTrue( response._strRedirectUrl.contains( Constants.ERROR_TYPE_INVALID_STATE ) );
    }
}
