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
package fr.paris.lutece.plugins.oauth2.business;

import java.io.Serializable;


/**
 * ServerConfiguration
 */
public class ServerConfiguration implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String _strIssuer;
    private String _strAuthorizationEndpointUri;
    private String _strTokenEndpointUri;
    private String _strUserInfoUri;
    private String _strUserInfoTokenMethod;

    /**
     * Returns the Issuer
     *
     * @return The Issuer
     */
    public String getIssuer(  )
    {
        return _strIssuer;
    }

    /**
     * Sets the Issuer
     *
     * @param strIssuer The Issuer
     */
    public void setIssuer( String strIssuer )
    {
        _strIssuer = strIssuer;
    }

    /**
     * Returns the AuthorizationEndpointUri
     *
     * @return The AuthorizationEndpointUri
     */
    public String getAuthorizationEndpointUri(  )
    {
        return _strAuthorizationEndpointUri;
    }

    /**
     * Sets the AuthorizationEndpointUri
     *
     * @param strAuthorizationEndpointUri The AuthorizationEndpointUri
     */
    public void setAuthorizationEndpointUri( String strAuthorizationEndpointUri )
    {
        _strAuthorizationEndpointUri = strAuthorizationEndpointUri;
    }

    /**
     * Returns the TokenEndpointUri
     *
     * @return The TokenEndpointUri
     */
    public String getTokenEndpointUri(  )
    {
        return _strTokenEndpointUri;
    }

    /**
     * Sets the TokenEndpointUri
     *
     * @param strTokenEndpointUri The TokenEndpointUri
     */
    public void setTokenEndpointUri( String strTokenEndpointUri )
    {
        _strTokenEndpointUri = strTokenEndpointUri;
    }

    /**
     * Returns the UserInfoUri
     *
     * @return The UserInfoUri
     */
    public String getUserInfoUri(  )
    {
        return _strUserInfoUri;
    }

    /**
     * Sets the UserInfoUri
     *
     * @param strUserInfoUri The UserInfoUri
     */
    public void setUserInfoUri( String strUserInfoUri )
    {
        _strUserInfoUri = strUserInfoUri;
    }

    /**
     * Returns the UserInfoTokenMethod
     *
     * @return The UserInfoTokenMethod
     */
    public String getUserInfoTokenMethod(  )
    {
        return _strUserInfoTokenMethod;
    }

    /**
     * Sets the UserInfoTokenMethod
     *
     * @param strUserInfoTokenMethod The UserInfoTokenMethod
     */
    public void setUserInfoTokenMethod( String strUserInfoTokenMethod )
    {
        _strUserInfoTokenMethod = strUserInfoTokenMethod;
    }
}
