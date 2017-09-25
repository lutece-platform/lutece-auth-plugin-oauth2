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
package fr.paris.lutece.plugins.oauth2.service;

import fr.paris.lutece.plugins.oauth2.business.AuthClientConf;
import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.business.Token;
import fr.paris.lutece.plugins.oauth2.jwt.JWTParser;
import fr.paris.lutece.plugins.oauth2.jwt.TokenValidationException;
import fr.paris.lutece.plugins.oauth2.web.Constants;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import org.apache.log4j.Logger;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;


/**
 * TokenService
 */
public final class TokenService
{
    private static final String BEAN_JWT_PARSER = "franceconnect.jwtParser";

    private static Logger _logger = Logger.getLogger( Constants.LOGGER_OAUTH2 );

    /**
     * private constructor
     */
    private TokenService(  )
    {
    }

    /**
     * parse the JSON for a token
     *
     * @param strJson The JSON
     * @param clientConfig The client configuration
     * @param serverConfig The server configuration
     * @param strStoredNonce The stored nonce
     * @return The Token
     * @throws java.io.IOException if an error occurs
     * @throws TokenValidationException If the token validation failed
     */
    public static Token parse( String strJson, AuthClientConf clientConfig, AuthServerConf serverConfig,JWTParser jwtParser,
        String strStoredNonce ) throws IOException, TokenValidationException
    {
        Token token = parseToken( strJson );

        _logger.debug( token );
        
        if(jwtParser!=null && serverConfig.isEnableJwtParser())
        {
        	jwtParser.parseJWT( token, clientConfig, serverConfig, strStoredNonce, _logger );
        }	
        return token;
    }

    /**
     * Parse the Token from a JSON string
     * @param strJson The JSON string
     * @return The Token
     * @throws IOException if an error occurs
     */
    static Token parseToken( String strJson ) throws IOException
    {
        return MapperService.parse( strJson, Token.class );
    }
}
