/*
 * Copyright (c) 2002-2026, City of Paris
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.enterprise.inject.Vetoed;

/**
 * Oauth2HandleErrorXPage Test: the error page of a failed sign-in is shown to a user who is not signed in
 */
public class Oauth2HandleErrorXPageTest extends LuteceTestCase
{
    private static final String ERROR = "access_denied";
    private static final String MARK_ERROR = "error";

    /**
     * XPage that keeps the model of the page instead of rendering the template, whose rendering is tested by
     * Oauth2HandleErrorRenderTest (not a CDI bean)
     */
    @Vetoed
    private static class ModelXPage extends Oauth2HandleErrorXPage
    {
        private static final long serialVersionUID = 1L;
        private final Map<String, Object> _model = new HashMap<>( );

        @Override
        protected Map<String, Object> getModel( )
        {
            return _model;
        }

        @Override
        protected XPage getXPage( String strTemplate, Locale locale, Map<String, Object> model )
        {
            return new XPage( );
        }
    }

    /**
     * An anonymous user gets the error page with the error, instead of being asked to sign in.
     *
     * @throws Exception
     *             if the page cannot be built
     */
    @Test
    public void testErrorPageForAnonymousUser( ) throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Constants.PARAMETER_ERROR, ERROR );
        ModelXPage xpage = new ModelXPage( );

        assertNotNull( xpage.getHomePage( request ) );
        assertEquals( ERROR, xpage._model.get( MARK_ERROR ) );
    }
}
