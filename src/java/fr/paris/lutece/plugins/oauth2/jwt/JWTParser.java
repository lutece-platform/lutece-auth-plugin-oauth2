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
package fr.paris.lutece.plugins.oauth2.jwt;

import org.apache.log4j.Logger;

import fr.paris.lutece.plugins.oauth2.business.AuthClientConf;
import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.business.Token;

/**
 * JWTParser
 */
public interface JWTParser
{
    /**
     * Extract Json Web Token from the token
     *
     * @param token
     *            The token
     * @param clientConfig
     *            The client configuration
     * @param serverConfig
     *            The server configuration
     * @param strStoredNonce
     *            The stored nonce
     * @param logger
     *            The logger
     * @throws TokenValidationException
     *             if an error occurs
     */
    void parseJWT( Token token, AuthClientConf clientConfig, AuthServerConf serverConfig, String strStoredNonce, Logger logger )
            throws TokenValidationException;
   
    /**
     * Extract Claims from the jwt
     *
     * @param strJwt
     *            The jwt
     * @param clientConfig
     *            The client configuration
     * @param serverConfig
     *            The server configuration
     * @param logger
     *            The logger
     * @throws TokenValidationException
     *             if an error occurs
     */
    String parseJWT( String strJwt, AuthClientConf clientConfig, AuthServerConf serverConfig, Logger logger )
            throws TokenValidationException;
    
}
