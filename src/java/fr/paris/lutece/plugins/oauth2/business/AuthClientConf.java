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

import com.nimbusds.jose.Algorithm;

import java.io.Serializable;

/**
 * OAuth RegisteredClient
 */
public class AuthClientConf implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String _strClientId;
    private String _strClientSecret;
    private String _strRedirectUri;

    /**
     * Returns the ClientId
     * 
     * @return The ClientId
     */
    public String getClientId( )
    {
        return _strClientId;
    }

    /**
     * Sets the ClientId
     * 
     * @param strClientId
     *            The ClientId
     */
    public void setClientId( String strClientId )
    {
        _strClientId = strClientId;
    }

    /**
     * Returns the ClientSecret
     * 
     * @return The ClientSecret
     */
    public String getClientSecret( )
    {
        return _strClientSecret;
    }

    /**
     * Sets the ClientSecret
     * 
     * @param strClientSecret
     *            The ClientSecret
     */
    public void setClientSecret( String strClientSecret )
    {
        _strClientSecret = strClientSecret;
    }

    /**
     * Returns the RedirectUri
     * 
     * @return The RedirectUri
     */
    public String getRedirectUri( )
    {
        return _strRedirectUri;
    }

    /**
     * Sets the RedirectUri
     * 
     * @param strRedirectUri
     *            The RedirectUri
     */
    public void setRedirectUri( String strRedirectUri )
    {
        _strRedirectUri = strRedirectUri;
    }

    /**
     * Return IDToken signed response Algorithm
     * 
     * @return The IDToken signed response Algorithm
     */
    public Algorithm getIdTokenSignedResponseAlg( )
    {
        return null;
    }
}
