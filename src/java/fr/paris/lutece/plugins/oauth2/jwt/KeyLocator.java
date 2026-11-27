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

import java.io.Serializable;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.paris.lutece.plugins.oauth2.web.Constants;

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.LocatorAdapter;
import io.jsonwebtoken.security.Jwk;
import io.jsonwebtoken.security.Jwks;

/**
 * Key locator which fetches keys from a JWKS endpoint
 */
public class KeyLocator extends LocatorAdapter<Key> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final String PARAMETER_KEYS = "keys";
    private static final String PARAMETER_USE = "use";
    private static final ObjectMapper MAPPER = new ObjectMapper( );
    private static final String USE_SIGNATURE = "sig";
    private static final Logger _logger = LogManager.getLogger( Constants.LOGGER_OAUTH2 );

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

    /**
     * Parses each key of a JWKS document on its own. The key set of jjwt is a set compared on the key material: the
     * same key published twice (with and without kid) is kept only once, possibly without its kid, so the keys are not
     * read through it. A key that cannot be parsed (unsupported type, invalid parameters) is skipped.
     *
     * @param strJwks
     *            the JWKS document
     * @return the keys, in the order of the document
     */
    private List<Jwk<?>> parseKeys( String strJwks )
    {
        List<Jwk<?>> listKeys = new ArrayList<>( );

        try
        {
            JsonNode nodeKeys = MAPPER.readTree( strJwks ).path( PARAMETER_KEYS );

            for ( JsonNode nodeKey : nodeKeys )
            {
                try
                {
                    listKeys.add( Jwks.parser( ).build( ).parse( MAPPER.writeValueAsString( nodeKey ) ) );
                }
                catch( RuntimeException ex )
                {
                    _logger.warn( "A key of the JWKS {} is skipped: {}", _strJwksEndpointUri, ex.getMessage( ) );
                }
            }
        }
        catch( JsonProcessingException ex )
        {
            _logger.error( "The JWKS {} is not valid JSON: {}", _strJwksEndpointUri, ex.getMessage( ) );
        }

        return listKeys;
    }

    private Key getKey( String keyId ) throws HttpAccessException
    {
        String jwks = _httpAccess.doGet( _strJwksEndpointUri );
        // the "kid" of a JWK is optional (RFC 7517): keys without one never match a token that names its key
        List<Jwk<?>> listCandidates = parseKeys( jwks ).stream( )
                .filter( k -> ( keyId == null ) || keyId.equals( k.getId( ) ) )
                .filter( k -> ( k.get( PARAMETER_USE ) == null ) || USE_SIGNATURE.equals( k.get( PARAMETER_USE ) ) )
                .collect( Collectors.toList( ) );

        if ( keyId != null && !listCandidates.isEmpty( ) )
        {
            return listCandidates.get( 0 ).toKey( );
        }
        if ( keyId == null && listCandidates.size( ) == 1 )
        {
            // a token without kid can only be verified when the set has exactly one usable key
            return listCandidates.get( 0 ).toKey( );
        }

        // no key: the parser refuses the token, which follows the normal error path of the sign-in
        _logger.error( "No key of the JWKS {} matches the id_token (kid: {}, candidate keys: {})", _strJwksEndpointUri, keyId, listCandidates.size( ) );
        return null;
    }

}
