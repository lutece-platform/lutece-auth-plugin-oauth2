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
package fr.paris.lutece.plugins.oauth2.service;

import fr.paris.lutece.plugins.oauth2.business.AuthClientConf;
import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.jwt.JWTParser;
import fr.paris.lutece.plugins.oauth2.jwt.JjwtJWTParser;
import fr.paris.lutece.plugins.oauth2.web.CallbackHandler;
import fr.paris.lutece.plugins.oauth2.dataclient.LogUserInfoDataClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Set;

/**
 * CDI Producer for OAuth2 Client-related beans
 */
@ApplicationScoped
public class Oauth2ClientBeansProducer
{
    /**
     * Produces the OAuth2 client configuration bean
     * 
     * @param clientId The OAuth2 client ID
     * @param clientSecret The OAuth2 client secret
     * @param isPublic Whether the client is public
     * @param isPkce Whether PKCE is enabled
     * @return The AuthClientConf instance
     */
    @Produces
    @ApplicationScoped
    @Named( "oauth2.client" )
    public AuthClientConf produceAuthClientConf(
        @ConfigProperty( name = "oauth2.clientId" ) String clientId,
        @ConfigProperty( name = "oauth2.clientSecret" ) String clientSecret,
        @ConfigProperty( name = "oauth2.client.public", defaultValue = "false" ) boolean isPublic,
        @ConfigProperty( name = "oauth2.client.pkce", defaultValue = "false" ) boolean isPkce )
    {
        AuthClientConf conf = new AuthClientConf( );
        conf.setClientId( clientId );
        conf.setClientSecret( clientSecret );
        conf.setPublic( isPublic );
        conf.setPkce( isPkce );
        return conf;
    }

    /**
     * Produces the JWT parser bean
     * 
     * @return The JWTParser instance
     */
    @Produces
    @ApplicationScoped
    @Named( "oauth2.jwtParser" )
    public JWTParser produceJWTParser( )
    {
        return new JjwtJWTParser( );
    }

    /**
     * Produces the callback handler bean
     * 
     * @param authServerConf The auth server configuration
     * @param authClientConf The auth client configuration
     * @param jwtParser The JWT parser
     * @return The CallbackHandler instance
     */
    @Produces
    @ApplicationScoped
    @Named( "oauth2.callbackHandler" )
    public CallbackHandler produceCallbackHandler(
        @Named( "oauth2.server" ) AuthServerConf authServerConf,
        @Named( "oauth2.client" ) AuthClientConf authClientConf,
        @Named( "oauth2.jwtParser" ) JWTParser jwtParser )
    {
        CallbackHandler handler = new CallbackHandler( );
        handler.setAuthServerConf( authServerConf );
        handler.setAuthClientConf( authClientConf );
        handler.setJWTParser( jwtParser );
        handler.setDefault( true );
        return handler;
    }

    /**
     * Produces the LogUserInfoDataClient bean
     * 
     * @param name The dataclient name
     * @param dataServerUri The data server URI
     * @param tokenMethod The token method
     * @param scopes The scopes as comma-separated string
     * @return The LogUserInfoDataClient instance
     */
    @Produces
    @ApplicationScoped
    @Named( "oauth2.logUserInfoDataClient" )
    public LogUserInfoDataClient produceLogUserInfoDataClient(
        @ConfigProperty( name = "oauth2.dataclient.logUserInfo.name", defaultValue = "logUserInfo" ) String name,
        @ConfigProperty( name = "oauth2.dataclient.logUserInfo.dataServerUri" ) String dataServerUri,
        @ConfigProperty( name = "oauth2.dataclient.logUserInfo.tokenMethod", defaultValue = "HEADER" ) String tokenMethod,
        @ConfigProperty( name = "oauth2.dataclient.logUserInfo.scopes", defaultValue = "openid,profile,email,address,phone" ) String scopes )
    {
        LogUserInfoDataClient client = new LogUserInfoDataClient( );
        client.setName( name );
        client.setDataServerUri( dataServerUri );
        client.setTokenMethod( tokenMethod );
        // Convert comma-separated scopes to Set
        Set<String> scopeSet = Set.of( scopes.split( "," ) );
        client.setScope( scopeSet );
        return client;
    }
}
