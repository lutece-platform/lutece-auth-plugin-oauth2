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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

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
     * @param redirectUri The OAuth2 client redirect URI
     * @param postLogoutRedirectUri The OAuth2 client post logout redirect URI
     * @return The AuthClientConf instance
     */
    @Produces
    @ApplicationScoped
    @Named( "oauth2.client" )
    public AuthClientConf produceAuthClientConf(
        @ConfigProperty( name = "oauth2.client.clientId" ) Optional<String> clientId,
        @ConfigProperty( name = "oauth2.client.clientSecret" ) Optional<String> clientSecret,
        @ConfigProperty( name = "oauth2.client.public", defaultValue = "false" ) boolean isPublic,
        @ConfigProperty( name = "oauth2.client.pkce", defaultValue = "false" ) boolean isPkce,
        @ConfigProperty( name = "oauth2.client.redirectUri" ) Optional<String> redirectUri,
        @ConfigProperty( name = "oauth2.client.postLogoutRedirectUri" ) Optional<String> postLogoutRedirectUri )
    {
        return new AuthClientConf(
            clientId.orElse( "" ),
            clientSecret.orElse( "" ),
            redirectUri.orElse( "" ),
            isPublic,
            isPkce,
            postLogoutRedirectUri.orElse( "" )
        );
    }

   

   
   
}
