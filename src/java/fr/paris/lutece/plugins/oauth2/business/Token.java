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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Token
 */
public class Token
{
    @JsonProperty( "access_token" )
    private String _strAccessToken;
    @JsonProperty( "token_type" )
    private String _strTokenType;
    @JsonProperty( "expires_in" )
    private int _nExpiresIn;
    @JsonProperty( "id_token" )
    private String _strIdToken;
    private IDToken _idToken;
    @JsonProperty( "refresh_token" )
    private String _strRefreshToken;

    /**
     * Returns the AccessToken
     *
     * @return The AccessToken
     */
    public String getAccessToken( )
    {
        return _strAccessToken;
    }

    /**
     * Sets the AccessToken
     *
     * @param strAccessToken
     *            The AccessToken
     */
    public void setAccessToken( String strAccessToken )
    {
        _strAccessToken = strAccessToken;
    }

    /**
     * Returns the TokenType
     *
     * @return The TokenType
     */
    public String getTokenType( )
    {
        return _strTokenType;
    }

    /**
     * Sets the TokenType
     *
     * @param strTokenType
     *            The TokenType
     */
    public void setTokenType( String strTokenType )
    {
        _strTokenType = strTokenType;
    }

    /**
     * Returns the ExpiresIn
     *
     * @return The ExpiresIn
     */
    public int getExpiresIn( )
    {
        return _nExpiresIn;
    }

    /**
     * Sets the ExpiresIn
     *
     * @param nExpiresIn
     *            The ExpiresIn
     */
    public void setExpiresIn( int nExpiresIn )
    {
        _nExpiresIn = nExpiresIn;
    }

    /**
     * Returns the IdToken
     *
     * @return The IdToken
     */
    public String getIdTokenString( )
    {
        return _strIdToken;
    }

    /**
     * Sets the IdToken
     *
     * @param strIdToken
     *            The IdToken
     */
    public void setIdTokenString( String strIdToken )
    {
        _strIdToken = strIdToken;
    }

    /**
     * Returns the IdToken
     *
     * @return The IdToken
     */
    public IDToken getIdToken( )
    {
        return _idToken;
    }

    /**
     * Sets the IdToken
     *
     * @param idToken
     *            The IdToken
     */
    public void setIdToken( IDToken idToken )
    {
        _idToken = idToken;
    }

    /**
     * get Refresh Token
     * 
     * @return get Refresh Token
     */
    public String getRefreshToken( )
    {
        return _strRefreshToken;
    }

    /**
     * set Refresh Token
     * 
     * @param strRefreshToken
     *            the Refresh Token
     */
    public void setRefreshToken( String strRefreshToken )
    {
        this._strRefreshToken = strRefreshToken;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString( )
    {
        StringBuilder sbToken = new StringBuilder( );
        sbToken.append( "Token infos : \n  access_token : " ).append( _strAccessToken ).append( "\n  expires_in : " ).append( _nExpiresIn )
                .append( "\n  token_type : " ).append( _strTokenType ).append( "\n  id_token : " ).append( _strIdToken ).append( "\n\n" );

        return sbToken.toString( );
    }

}
