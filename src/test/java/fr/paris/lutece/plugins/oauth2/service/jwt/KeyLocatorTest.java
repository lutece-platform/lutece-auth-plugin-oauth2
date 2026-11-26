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
package fr.paris.lutece.plugins.oauth2.service.jwt;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.oauth2.jwt.KeyLocator;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Jwks;
import jakarta.enterprise.inject.Vetoed;

/**
 * Keys of a JWKS set without "kid" (optional, RFC 7517) must not make the id_token validation fail with an NPE (AX-10b)
 */
public class KeyLocatorTest extends LuteceTestCase
{
    private static final String JWKS_URI = "http://jwks.test/jwks";

    @Vetoed
    private static class StubHttpAccess extends HttpAccess
    {
        private final String _strJwks;

        StubHttpAccess( String strJwks )
        {
            _strJwks = strJwks;
        }

        @Override
        public String doGet( String strUrl )
        {
            return _strJwks;
        }
    }

    private static String jwks( KeyPair... pairs ) throws Exception
    {
        return jwks( null, pairs );
    }

    /**
     * @param ids
     *            the kid of each key, null for none
     */
    private static String jwks( String [ ] ids, KeyPair... pairs ) throws Exception
    {
        List<Map<String, ?>> keys = new ArrayList<>( );
        for ( int i = 0; i < pairs.length; i++ )
        {
            String strId = ids == null ? null : ids [i];
            keys.add( strId == null ? Jwks.builder( ).key( pairs [i].getPublic( ) ).build( )
                    : Jwks.builder( ).key( pairs [i].getPublic( ) ).id( strId ).build( ) );
        }
        return new ObjectMapper( ).writeValueAsString( Map.of( "keys", keys ) );
    }

    private static void parse( String strJwt, String strJwks )
    {
        Jwts.parser( ).keyLocator( new KeyLocator( JWKS_URI, new StubHttpAccess( strJwks ) ) ).build( ).parseSignedClaims( strJwt );
    }

    private static KeyPair rsa( )
    {
        return Jwts.SIG.RS256.keyPair( ).build( );
    }

    /**
     * Token with a kid, the JWKS has first a key without kid then the matching key
     */
    @Test
    public void testKeyWithoutKidIsSkipped( ) throws Exception
    {
        KeyPair other = rsa( );
        KeyPair signer = rsa( );
        String strJwt = Jwts.builder( ).header( ).keyId( "k1" ).and( ).subject( "s" ).signWith( signer.getPrivate( ) ).compact( );

        assertDoesNotThrow( ( ) -> parse( strJwt, jwks( new String [ ] { null, "k1" }, other, signer ) ) );
    }

    /**
     * Token without kid, the JWKS has exactly one key: it is used
     */
    @Test
    public void testTokenWithoutKidUsesTheOnlyKey( ) throws Exception
    {
        KeyPair signer = rsa( );
        String strJwt = Jwts.builder( ).subject( "s" ).signWith( signer.getPrivate( ) ).compact( );

        assertDoesNotThrow( ( ) -> parse( strJwt, jwks( signer ) ) );
    }

    /**
     * Token without kid, several keys: no key is chosen, the token is refused by the parser (no NullPointerException)
     */
    @Test
    public void testTokenWithoutKidAndSeveralKeysIsRefused( ) throws Exception
    {
        KeyPair signer = rsa( );
        String strJwt = Jwts.builder( ).subject( "s" ).signWith( signer.getPrivate( ) ).compact( );
        String strJwks = jwks( rsa( ), signer );

        assertThrows( JwtException.class, ( ) -> parse( strJwt, strJwks ) );
    }

    /**
     * Signature verification is unchanged: the only key of the set, if it is not the signer's, refuses the token
     */
    @Test
    public void testOnlyKeyOfAnotherSignerIsRefused( ) throws Exception
    {
        String strJwt = Jwts.builder( ).subject( "s" ).signWith( rsa( ).getPrivate( ) ).compact( );
        String strJwks = jwks( rsa( ) );

        assertThrows( JwtException.class, ( ) -> parse( strJwt, strJwks ) );
    }
}
