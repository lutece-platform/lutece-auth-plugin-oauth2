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
package fr.paris.lutece.plugins.oauth2.web;

import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * France Connect XPage Application
 */
@Controller( xpageName = "oauth2HandleError", pagePathI18nKey = "oauth2.xpage.oauth2HandleErrorPath", pageTitleI18nKey = "oauth2.xpage.oauth2HandleErrorTitle" )
public class Oauth2HandleErrorXPage extends MVCApplication
{
    /**
     * 
     */
    private static final long   serialVersionUID      = 1L;

    // Views
    private static final String VIEW_HOME             = "error";

    // Templates
    private static final String TEMPLATE_HANDLE_ERROR = "skin/plugins/oauth2/handle_error.html";

    // Markers
    private static final String MARK_USER             = "user";
    private static final String MARK_ERROR            = "error";

    // Parameters
    private static final String PARAMETER_ERROR       = "error";

    /**
     * Build the Login page
     * 
     * @param request
     *            The HTTP request
     * @return The XPage object containing the page content
     * @throws UserNotSignedException
     */
    @View( value = VIEW_HOME, defaultView = true )
    public XPage getHomePage( HttpServletRequest request ) throws UserNotSignedException
    {
        Map<String, Object> model = getModel( );

        String strError = request.getParameter( PARAMETER_ERROR );

        LuteceUser user = checkUserAuthentication( request );

        model.put( MARK_USER, user );
        model.put( MARK_ERROR, strError );

        return getXPage( TEMPLATE_HANDLE_ERROR, request.getLocale( ), model );
    }

    /**
     * check if user is authenticated
     *
     * @param request
     *            request
     * @return The connected Lutece user
     * @throws UserNotSignedException
     *             if user is not connected
     */
    private LuteceUser checkUserAuthentication( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser luteceUser = SecurityService.isAuthenticationEnable( ) ? SecurityService.getInstance( ).getRegisteredUser( request ) : null;

        if ( luteceUser == null )
        {
            throw new UserNotSignedException( );
        }
        return luteceUser;
    }
}
