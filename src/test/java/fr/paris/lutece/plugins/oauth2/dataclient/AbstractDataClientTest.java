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
package fr.paris.lutece.plugins.oauth2.dataclient;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.oauth2.web.Constants;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.enterprise.inject.Vetoed;

/**
 * AbstractDataClient Test: the error given by the authorization server is put in the URL of the error page as one
 * parameter
 */
public class AbstractDataClientTest extends LuteceTestCase
{
    private static final String ERROR = "a&b c=d";

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
     * Reads a parameter of the query string of an URL, as a servlet container does
     *
     * @param strUrl
     *            the URL
     * @param strName
     *            the parameter name
     * @return the decoded value of the first parameter with this name, or null
     */
    public static String getParameter( String strUrl, String strName )
    {
        String strQuery = strUrl.substring( strUrl.indexOf( '?' ) + 1 );

        for ( String strPair : strQuery.split( "&" ) )
        {
            int nIndex = strPair.indexOf( '=' );

            if ( nIndex > 0 && strPair.substring( 0, nIndex ).equals( strName ) )
            {
                return URLDecoder.decode( strPair.substring( nIndex + 1 ), StandardCharsets.UTF_8 );
            }
        }

        return null;
    }

    @Test
    public void testHandleErrorKeepsTheWholeError( )
    {
        RedirectResponse response = new RedirectResponse( );

        new LogUserInfoDataClient( ).handleError( new MockHttpServletRequest( ), response, ERROR );

        assertNotNull( response._strRedirectUrl );
        assertEquals( ERROR, getParameter( response._strRedirectUrl, Constants.PARAMETER_ERROR ), response._strRedirectUrl );
    }
}
