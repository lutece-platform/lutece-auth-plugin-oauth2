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
package fr.paris.lutece.plugins.oauth2.jwt;

import java.security.Key;

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.LocatorAdapter;
import io.jsonwebtoken.security.JwkSet;
import io.jsonwebtoken.security.Jwks;

/**
 * Key locator which fetches keys from a JWKS endpoint
 */
public class KeyLocator extends LocatorAdapter<Key>
{
    private final String _strJwksEndpointUri;
    private final HttpAccess _httpAccess;

    /**
     * Constructs a Key Locator
     * 
     * @param strJwksEndpointUri
     *            the URI of the JKWS resource
     * @param httpAccess
     *            the httpAccess for fetching the file
     */
    public KeyLocator( String strJwksEndpointUri, HttpAccess httpAccess )
    {
        _strJwksEndpointUri = strJwksEndpointUri;
        _httpAccess = httpAccess;
    }

    @Override
    protected Key locate( JwsHeader header )
    {
        try
        {
            return getKey( header.getKeyId( ) );
        }
        catch( HttpAccessException e )
        {
            throw new AppException( e.getMessage( ), e );
        }
    }

    private Key getKey( String keyId ) throws HttpAccessException
    {
        String jwks = _httpAccess.doGet( _strJwksEndpointUri );
        JwkSet jwkSet = Jwks.setParser( ).build( ).parse( jwks );
        return jwkSet.getKeys( ).stream( ).filter( k -> k.getId( ).equals( keyId ) ).map( k -> k.toKey( ) ).findFirst( ).orElse( null );
    }

}
