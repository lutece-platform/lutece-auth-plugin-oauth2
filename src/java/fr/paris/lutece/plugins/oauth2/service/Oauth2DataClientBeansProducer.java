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

import fr.paris.lutece.plugins.oauth2.dataclient.LogUserInfoDataClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * CDI Producer for OAuth2 DataClient-related beans
 */
@ApplicationScoped
public class Oauth2DataClientBeansProducer
{
    /**
     * Produces the LogUserInfoDataClient bean
     * 
     * @param name The data client name
     * @param dataServerUri The data server URI
     * @param tokenMethod The token method (e.g., HEADER)
     * @param scopes The scopes as comma-separated string
     * @param acrValues Optional ACR values as comma-separated string
     * @return The LogUserInfoDataClient instance
     */
    @Produces
    @ApplicationScoped
    @Named( "oauth2.logUserInfoDataClient" )
    public LogUserInfoDataClient produceLogUserInfoDataClient(
        @ConfigProperty( name = "oauth2.dataclient.logUserInfo.dataServerUri" ) Optional<String> dataServerUri,
        @ConfigProperty( name = "oauth2.dataclient.logUserInfo.tokenMethod" ) Optional<String> tokenMethod,
        @ConfigProperty( name = "oauth2.dataclient.logUserInfo.scopes" ) Optional<String> scopes,
        @ConfigProperty( name = "oauth2.dataclient.logUserInfo.acrValues" ) Optional<String> acrValues )
    {
        LogUserInfoDataClient client = new LogUserInfoDataClient( );
        client.setName( "logUserInfo" );
        
        if ( dataServerUri.isPresent( ) )
        {
            client.setDataServerUri( dataServerUri.get( ) );
        }
        
        if ( tokenMethod.isPresent( ) )
        {
            client.setTokenMethod( tokenMethod.get( ) );
        }
        
        // Convert comma-separated scopes to Set
        if ( scopes.isPresent( ) && !scopes.get( ).isEmpty( ) )
        {
            Set<String> scopeSet = new HashSet<>( Arrays.asList( scopes.get( ).split( "," ) ) );
            client.setScope( scopeSet );
        }
        
        // Optional: Set ACR values if provided
        if ( acrValues.isPresent( ) && !acrValues.get( ).isEmpty( ) )
        {
            Set<String> acrValuesSet = new HashSet<>( Arrays.asList( acrValues.get( ).split( "," ) ) );
            client.setAcrValuesSet( acrValuesSet );
        }
        
        return client;
    }
}
