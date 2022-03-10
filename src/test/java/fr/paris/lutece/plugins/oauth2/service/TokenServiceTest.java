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
package fr.paris.lutece.plugins.oauth2.service;

import fr.paris.lutece.plugins.oauth2.business.Token;
import fr.paris.lutece.test.LuteceTestCase;

import org.junit.Test;

import java.io.IOException;

/**
 * TokenService Test
 */
public class TokenServiceTest extends LuteceTestCase
{
    private static final String JSON_TOKEN = "{ \"access_token\" : \"608c2c4c250f9dcd118dc087cb23b2c4db2a848161044b03\", \"token_type\" : \"Bearer\", \"expires_in\" : 3600, \"id_token\" : \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vZmNwLmludGVnMDEuZGV2LWZyYW5jZWNvbm5lY3QuZnIiLCJzdWIiOiIwMTI2MzIzNDM2MjFjMjYwMGY0M2I1YWIxOTM2NzQzZGZjOGExOTljZWNhODUxYTciLCJhdWQiOiJhOWEyNTg5NWY5ZDc2ZjZjODlhYTIxODMwNTc1YmYzNGIzZjRmNjg0YTcyYTg0YzEzYWIxYzM4MTA2NDNkODU5IiwiZXhwIjoxNDMyOTM1MTM5LCJpYXQiOjE0MzI5MzE1MzksIm5vbmNlIjoiMTNjMWMyMDk5ODlmMSIsImlkcCI6ImRnZmlwIiwiYWNyIjoiZWlkYXMyIn0.RrzwbO0ygvMbFJYYvzsx530IiJpj3iQ44GQPcpTHIKM\" }";

    /**
     * Test of parse method, of class TokenService.
     * 
     * @throws java.io.IOException
     */
    @Test
    public void testParseToken( ) throws IOException
    {
        System.out.println( "parseTokenJson" );

        Token token = TokenService.getService( ).parseToken( JSON_TOKEN );

        assertEquals( token.getAccessToken( ), "608c2c4c250f9dcd118dc087cb23b2c4db2a848161044b03" );
        assertEquals( token.getExpiresIn( ), 3600 );
        assertEquals( token.getTokenType( ), "Bearer" );
    }
}
