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

import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.business.OIDCAuthServerConf;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * CDI Producer for OAuth2 Server-related beans
 */
@ApplicationScoped
public class Oauth2ServerBeansProducer
{
    /**
     * Produces the OAuth2 server configuration bean
     * 
     * @param issuer The OAuth2 issuer URL
     * @return The AuthServerConf instance
     */
    @Produces
    @ApplicationScoped
    @Named( "oauth2.oIDCServer" )
    public AuthServerConf produceOIDCAuthServerConf( 
        @ConfigProperty( name = "oauth2.oIDCServer.issuer" ) Optional<String> issuer )
    {
        OIDCAuthServerConf conf = new OIDCAuthServerConf( );
        conf.setName("oauth2.oIDCServer");
        if ( issuer.isPresent( ) )
        {
            conf.setIssuer( issuer.get( ) );
        }
        return conf;
    }


     /**
     * Produces the OAuth2 server configuration bean
     * 
     * @param issuer The OAuth2 issuer URL
     * @return The AuthServerConf instance
     */
    @Produces
    @ApplicationScoped
    @Named( "oauth2.server" )
    public AuthServerConf produceAuthServerConf( 
        @ConfigProperty( name = "oauth2.server.issuer" ) Optional<String> issuer,
        @ConfigProperty( name = "oauth2.server.authorizationEndpointUri" ) Optional<String> authorizationEndpointUri,
        @ConfigProperty( name = "oauth2.server.tokenEndpointUri" ) Optional<String> tokenEndpointUri,
        @ConfigProperty( name = "oauth2.server.logoutEndpointUri" ) Optional<String> logoutEndpointUri,
        @ConfigProperty( name = "oauth2.server.enableJwtParser", defaultValue = "false" ) boolean enableJwtParser,
        @ConfigProperty( name = "oauth2.server.iDTokenSignatureAlgorithmNames" ) Optional<String> idTokenSignatureAlgorithmNames ,
        @ConfigProperty( name = "oauth2.server.jwksEndpointUri" ) Optional<String> jwksEndpointUri
    )
    {
        Set<String> algorithms = idTokenSignatureAlgorithmNames
            .map( names -> Stream.of( names.split( "," ) )
                .map( String::trim )
                .collect( Collectors.toSet( ) ) )
            .orElse( Set.of( ) );

        return new AuthServerConf(
            "oauth2.server",
            issuer.orElse( "" ),
            authorizationEndpointUri.orElse( "" ),
            tokenEndpointUri.orElse( "" ),
            logoutEndpointUri.orElse( "" ),
            enableJwtParser,
            algorithms,
            jwksEndpointUri.orElse( "" )
        );
    }
}
