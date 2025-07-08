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
package fr.paris.lutece.plugins.oauth2.business;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.oauth2.service.CachingHttpAccessService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.httpaccess.HttpAccessService;
import fr.paris.lutece.util.httpaccess.PropertiesHttpClientConfiguration;

/**
 * Server configuration for OpenID Connect
 * 
 * @since 2.0.0
 */
public class OIDCAuthServerConf extends AuthServerConf
{
    private static final long serialVersionUID = 3459341547945895738L;
    private static final String WELLKNOWN_PATH = ".well-known/openid-configuration";

    private final HttpAccess _httpAccess;
    private final ObjectMapper _mapper;

    public OIDCAuthServerConf( )
    {
        HttpAccessService accessService = new CachingHttpAccessService( new PropertiesHttpClientConfiguration( ) );
        this._httpAccess = new HttpAccess( accessService );
        this._mapper = new ObjectMapper( );
        this._mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
    }

    @Override
    public boolean isEnableJwtParser( )
    {
        return true;
    }

    @Override
    public String getAuthorizationEndpointUri( )
    {
        if ( super.getAuthorizationEndpointUri( ) != null )
        {
            return super.getAuthorizationEndpointUri( );
        }
        return getOpenidConfiguration( ).getAuthorizationEndpoint( );
    }

    @Override
    public String getTokenEndpointUri( )
    {
        if ( super.getTokenEndpointUri( ) != null )
        {
            return super.getTokenEndpointUri( );
        }
        return getOpenidConfiguration( ).getTokenEndpoint( );
    }

    @Override
    public Set<String> getIDTokenSignatureAlgorithmNames( )
    {
        if ( super.getIDTokenSignatureAlgorithmNames( ) != null )
        {
            return super.getIDTokenSignatureAlgorithmNames( );
        }
        return Set.of( getOpenidConfiguration( ).getIDTokenSigningAlgValuesSupported( ) );
    }

    @Override
    public String getJwksEndpointUri( )
    {
        if ( super.getJwksEndpointUri( ) != null )
        {
            return super.getJwksEndpointUri( );
        }
        return getOpenidConfiguration( ).getJwksURI( );
    }

    @Override
    public String getLogoutEndpointUri( )
    {
        if ( super.getLogoutEndpointUri( ) != null )
        {
            return super.getLogoutEndpointUri( );
        }
        return getOpenidConfiguration( ).getEndSessionEndpoint( );
    }

    private OpenIDConfiguration getOpenidConfiguration( )
    {
        String issuer = getIssuer( );
        Objects.requireNonNull( issuer, "issuer must not be null" );
        if ( !issuer.startsWith( "https" ) )
        {
            throw new UnsupportedOperationException( "The issuer must start with https, but is " + issuer );
        }
        String strConfURL;
        if ( issuer.endsWith( "/" ) )
        {
            strConfURL = issuer + WELLKNOWN_PATH;
        }
        else
        {
            strConfURL = issuer + "/" + WELLKNOWN_PATH;
        }
        try
        {
            String strConfiguration = _httpAccess.doGet( strConfURL );
            OpenIDConfiguration res = this._mapper.readValue( strConfiguration, OpenIDConfiguration.class );
            res.validate( issuer );
            return res;
        }
        catch( HttpAccessException | IOException e )
        {
            throw new AppException( e.getMessage( ), e );
        }
    }
}
