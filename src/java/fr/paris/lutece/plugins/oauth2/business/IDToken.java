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
package fr.paris.lutece.plugins.oauth2.business;

/**
 * IDToken : informations provided by a JWT (Json Web Token)
 */
public class IDToken
{
    private String _strAudience;
    private String _strExpiration;
    private String _strIssueAt;
    private String _strIssuer;
    private String _strSubject;
    private String _strIdProvider;
    private String _strNonce;
    private String _strAcr;

    /**
     * Returns the Audience
     * 
     * @return The Audience
     */
    public String getAudience( )
    {
        return _strAudience;
    }

    /**
     * Sets the Audience
     * 
     * @param strAudience
     *            The Audience
     */
    public void setAudience( String strAudience )
    {
        _strAudience = strAudience;
    }

    /**
     * Returns the Expiration
     * 
     * @return The Expiration
     */
    public String getExpiration( )
    {
        return _strExpiration;
    }

    /**
     * Sets the Expiration
     * 
     * @param strExpiration
     *            The Expiration
     */
    public void setExpiration( String strExpiration )
    {
        _strExpiration = strExpiration;
    }

    /**
     * Returns the IssueAt
     * 
     * @return The IssueAt
     */
    public String getIssueAt( )
    {
        return _strIssueAt;
    }

    /**
     * Sets the IssueAt
     * 
     * @param strIssueAt
     *            The IssueAt
     */
    public void setIssueAt( String strIssueAt )
    {
        _strIssueAt = strIssueAt;
    }

    /**
     * Returns the Issuer
     * 
     * @return The Issuer
     */
    public String getIssuer( )
    {
        return _strIssuer;
    }

    /**
     * Sets the Issuer
     * 
     * @param strIssuer
     *            The Issuer
     */
    public void setIssuer( String strIssuer )
    {
        _strIssuer = strIssuer;
    }

    /**
     * Returns the Subject
     * 
     * @return The Subject
     */
    public String getSubject( )
    {
        return _strSubject;
    }

    /**
     * Sets the Subject
     * 
     * @param strSubject
     *            The Subject
     */
    public void setSubject( String strSubject )
    {
        _strSubject = strSubject;
    }

    /**
     * Returns the IdProvider
     * 
     * @return The IdProvider
     */
    public String getIdProvider( )
    {
        return _strIdProvider;
    }

    /**
     * Sets the IdProvider
     * 
     * @param strIdProvider
     *            The IdProvider
     */
    public void setIdProvider( String strIdProvider )
    {
        _strIdProvider = strIdProvider;
    }

    /**
     * Returns the Nonce
     * 
     * @return The Nonce
     */
    public String getNonce( )
    {
        return _strNonce;
    }

    /**
     * Sets the Nonce
     * 
     * @param strNonce
     *            The Nonce
     */
    public void setNonce( String strNonce )
    {
        _strNonce = strNonce;
    }

    /**
     * Returns the Acr
     * 
     * @return The Acr
     */
    public String getAcr( )
    {
        return _strAcr;
    }

    /**
     * Sets the Acr
     * 
     * @param strAcr
     *            The Acr
     */
    public void setAcr( String strAcr )
    {
        _strAcr = strAcr;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString( )
    {
        StringBuilder sbToken = new StringBuilder( );
        sbToken.append( "Token ID infos : \n  aud : " ).append( _strAudience ).append( "\n  exp : " ).append( _strExpiration ).append( "\n  iat : " )
                .append( _strIssueAt ).append( "\n  iss : " ).append( _strIssuer ).append( "\n  sub : " ).append( _strSubject ).append( "\n  idp : " )
                .append( _strIdProvider ).append( "\n  nonce : " ).append( _strNonce ).append( "\n  acr : " ).append( _strAcr ).append( "\n\n" );

        return sbToken.toString( );
    }
}
