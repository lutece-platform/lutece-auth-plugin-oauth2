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
public class AuthServerConf implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String _strName;
    private String _strIssuer;
    private String _strAuthorizationEndpointUri;
    private String _strTokenEndpointUri;
    private String _strLogoutEndpointUri;
    private boolean _bEnableJwtParser;
    
    
    /**
     * 
     * @return the servername
     */
	public String getName() {
		return _strName;
	}
	/**
	 * 
	 * @param _strName the servername
	 */
	public void setName(String strName) {
		this._strName = strName;
	}

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
     * Returns the LogoutEndpointUri
     *
     * @return The LogoutEndpointUri
     */
    public String getLogoutEndpointUri(  )
    {
        return _strLogoutEndpointUri;
    }

    /**
     * Sets the LogoutEndpointUri
     *
     * @param strLogoutEndpointUri The LogoutEndpointUri
     */
    public void setLogoutEndpointUri( String strLogoutEndpointUri )
    {
        _strLogoutEndpointUri = strLogoutEndpointUri;
    }
    /**
     * 
     * @return enable jwt parser
     */
	public boolean isEnableJwtParser() {
		return _bEnableJwtParser;
	}
	/**
	 * 
	 * @param _bEnableJwtParser enable jwt parser
	 */
	public void setEnableJwtParser(boolean _bEnableJwtParser) {
		this._bEnableJwtParser = _bEnableJwtParser;
	}

    
    
}
