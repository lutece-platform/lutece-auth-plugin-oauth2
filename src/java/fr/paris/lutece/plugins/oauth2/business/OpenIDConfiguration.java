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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.paris.lutece.portal.service.util.AppException;

/**
 * OpenID Configuration
 * 
 * @see https://openid.net/specs/openid-connect-discovery-1_0.html
 * @see https://openid.net/specs/openid-connect-rpinitiated-1_0.html
 * @since 2.0.0
 */
public class OpenIDConfiguration
{
    private String _strIssuer;
    private String _strAuthorizationEndpoint;
    private String _strTokenEndpoint;
    private String _strUserinfoEndpoint;
    private String _strJwksURI;
    private String _strRegistrationEndpoint;
    private String [ ] _scopesSupported;
    private String [ ] _responseTypesSupported;
    private String [ ] _responseModesSupported;
    private String [ ] _grantTypesSupported;
    private String [ ] _acrValuesSupported;
    private String [ ] _subjectTypesSupported;
    private String [ ] _idTokenSigningAlgValuesSupported;
    private String [ ] _idTokenEncryptionAlgValuesSupported;
    private String [ ] _idTokenEncryptionEncValuesSupported;
    private String [ ] _userinfoSigningAlgValuesSupported;
    private String [ ] _userinfoEncryptionAlgValuesSupported;
    private String [ ] _userinfoEncryptionEncValuesSupported;
    private String [ ] _requestObjectSigningAlgValuesSupported;
    private String [ ] _requestObjectEncryptionAlgValuesSupported;
    private String [ ] _requestObjectEncryptionEncValuesSupported;
    private String [ ] _tokenEndpointAuthMethodsSupported;
    private String [ ] _tokenEndpointAuthSigningAlgValuesSupported;
    private String [ ] _displayValuesSupported;
    private String [ ] _claimTypesSupported;
    private String [ ] _claimsSupported;
    private String _serviceDocumentation;
    private String [ ] _claimsLocalesSupported;
    private String [ ] _uiLocalesSupported;
    private boolean _claimsParameterSupported;
    private boolean _requestParameterSupported;
    private boolean _requestUIRParameterSupported = true;
    private boolean _requireRequestUIRRegistration;
    private String _opPolicyURI;
    private String _opTOSURI;
    private String _strEndSessionEndpoint;

    public String getIssuer( )
    {
        return _strIssuer;
    }

    public void setIssuer( String strIssuer )
    {
        _strIssuer = strIssuer;
    }

    @JsonProperty( "authorization_endpoint" )
    public String getAuthorizationEndpoint( )
    {
        return _strAuthorizationEndpoint;
    }

    @JsonProperty( "authorization_endpoint" )
    public void getAuthorizationEndpoint( String strAuthorizationEndpoint )
    {
        _strAuthorizationEndpoint = strAuthorizationEndpoint;
    }

    public String getTokenEndpoint( )
    {
        return _strTokenEndpoint;
    }

    @JsonProperty( "token_endpoint" )
    public void setTokenEndpoint( String strTokenEndpoint )
    {
        _strTokenEndpoint = strTokenEndpoint;
    }

    public String getUserinfoEndpoint( )
    {
        return _strUserinfoEndpoint;
    }

    @JsonProperty( "userinfo_endpoint" )
    public void setUserinfoEndpoint( String strUserinfoEndpoint )
    {
        _strUserinfoEndpoint = strUserinfoEndpoint;
    }

    public String getJwksURI( )
    {
        return _strJwksURI;
    }

    @JsonProperty( "jwks_uri" )
    public void setJwksUri( String strJwksURI )
    {
        _strJwksURI = strJwksURI;
    }

    public String getRegistrationEndpoint( )
    {
        return _strRegistrationEndpoint;
    }

    @JsonProperty( "registration_endpoint" )
    public void setRegistrationEndpoint( String strRegistrationEndpoint )
    {
        _strRegistrationEndpoint = strRegistrationEndpoint;
    }

    public String [ ] getScopesSupported( )
    {
        return _scopesSupported;
    }

    @JsonProperty( "scopes_supported" )
    public void setScopesSupported( String [ ] scopesSupported )
    {
        this._scopesSupported = scopesSupported;
    }

    public String [ ] getResponseTypesSupported( )
    {
        return _responseTypesSupported;
    }

    @JsonProperty( "response_types_supported" )
    public void setResponseTypesSupported( String [ ] responseTypesSupported )
    {
        this._responseTypesSupported = responseTypesSupported;
    }

    public String [ ] getResponseModesSupported( )
    {
        return _responseModesSupported;
    }

    @JsonProperty( "response_modes_supported" )
    public void setResponseModesSupported( String [ ] responseModesSupported )
    {
        this._responseModesSupported = responseModesSupported;
    }

    public String [ ] getGrantTypesSupported( )
    {
        return _grantTypesSupported;
    }

    @JsonProperty( "grant_types_supported" )
    public void setGrantTypesSupported( String [ ] grantTypesSupported )
    {
        this._grantTypesSupported = grantTypesSupported;
    }

    public String [ ] getAcrValuesSupported( )
    {
        return _acrValuesSupported;
    }

    @JsonProperty( "acr_values_supported" )
    public void setAcrValuesSupported( String [ ] acrValuesSupported )
    {
        this._acrValuesSupported = acrValuesSupported;
    }

    public String [ ] getSubjectTypesSupported( )
    {
        return _subjectTypesSupported;
    }

    @JsonProperty( "subject_types_supported" )
    public void setSubjectTypesSupported( String [ ] subjectTypesSupported )
    {
        this._subjectTypesSupported = subjectTypesSupported;
    }

    public String [ ] getIDTokenSigningAlgValuesSupported( )
    {
        return _idTokenSigningAlgValuesSupported;
    }

    @JsonProperty( "id_token_signing_alg_values_supported" )
    public void setIDTokenSigningAlgValuesSupported( String [ ] idTokenSigningAlgValuesSupported )
    {
        this._idTokenSigningAlgValuesSupported = idTokenSigningAlgValuesSupported;
    }

    public String [ ] getIDTokenEncryptionAlgValuesSupported( )
    {
        return _idTokenEncryptionAlgValuesSupported;
    }

    @JsonProperty( "id_token_encryption_alg_values_supported" )
    public void setIDTokenEncryptionAlgValuesSupported( String [ ] idTokenEncryptionAlgValuesSupported )
    {
        this._idTokenEncryptionAlgValuesSupported = idTokenEncryptionAlgValuesSupported;
    }

    public String [ ] getIDTokenEncryptionEncValuesSupported( )
    {
        return _idTokenEncryptionEncValuesSupported;
    }

    @JsonProperty( "id_token_encryption_enc_values_supported" )
    public void setIDTokenEncryptionEncValuesSupported( String [ ] _idTokenEncryptionEncValuesSupported )
    {
        this._idTokenEncryptionEncValuesSupported = _idTokenEncryptionEncValuesSupported;
    }

    public String [ ] getUserinfoSigningAlgValuesSupported( )
    {
        return _userinfoSigningAlgValuesSupported;
    }

    @JsonProperty( "userinfo_signing_alg_values_supported" )
    public void setUserinfoSigningAlgValuesSupported( String [ ] userinfoSigningAlgValuesSupported )
    {
        this._userinfoSigningAlgValuesSupported = userinfoSigningAlgValuesSupported;
    }

    public String [ ] getUserinfoEncryptionAlgValuesSupported( )
    {
        return _userinfoEncryptionAlgValuesSupported;
    }

    @JsonProperty( "userinfo_encryption_alg_values_supported" )
    public void setUserinfoEncryptionAlgValuesSupported( String [ ] userinfoEncryptionAlgValuesSupported )
    {
        this._userinfoEncryptionAlgValuesSupported = userinfoEncryptionAlgValuesSupported;
    }

    public String [ ] getUserinfoEncryptionEncValuesSupported( )
    {
        return _userinfoEncryptionEncValuesSupported;
    }

    @JsonProperty( "userinfo_encryption_enc_values_supported" )
    public void setUserinfoEncryptionEncValuesSupported( String [ ] userinfoEncryptionEncValuesSupported )
    {
        this._userinfoEncryptionEncValuesSupported = userinfoEncryptionEncValuesSupported;
    }

    public String [ ] getRequestObjectSigningAlgValuesSupported( )
    {
        return _requestObjectSigningAlgValuesSupported;
    }

    @JsonProperty( "request_object_signing_alg_values_supported" )
    public void setRequestObjectSigningAlgValuesSupported( String [ ] requestObjectSigningAlgValuesSupported )
    {
        this._requestObjectSigningAlgValuesSupported = requestObjectSigningAlgValuesSupported;
    }

    public String [ ] getRequestObjectEncryptionAlgValuesSupported( )
    {
        return _requestObjectEncryptionAlgValuesSupported;
    }

    @JsonProperty( "request_object_encryption_alg_values_supported" )
    public void setRequestObjectEncryptionAlgValuesSupported( String [ ] requestObjectEncryptionAlgValuesSupported )
    {
        this._requestObjectEncryptionAlgValuesSupported = requestObjectEncryptionAlgValuesSupported;
    }

    public String [ ] getRequestObjectEncryptionEncValuesSupported( )
    {
        return _requestObjectEncryptionEncValuesSupported;
    }

    @JsonProperty( "request_object_encryption_enc_values_supported" )
    public void setRequestObjectEncryptionEncValuesSupported( String [ ] requestObjectEncryptionEncValuesSupported )
    {
        this._requestObjectEncryptionEncValuesSupported = requestObjectEncryptionEncValuesSupported;
    }

    public String [ ] getTokenEndpointAuthMethodsSupported( )
    {
        return _tokenEndpointAuthMethodsSupported;
    }

    @JsonProperty( "token_endpoint_auth_methods_supported" )
    public void setTokenEndpointAuthMethodsSupported( String [ ] tokenEndpointAuthMethodsSupported )
    {
        this._tokenEndpointAuthMethodsSupported = tokenEndpointAuthMethodsSupported;
    }

    public String [ ] getTokenEndpointAuthSigningAlgValuesSupported( )
    {
        return _tokenEndpointAuthSigningAlgValuesSupported;
    }

    @JsonProperty( "token_endpoint_auth_signing_alg_values_supported" )
    public void setTokenEndpointAuthSigningAlgValuesSupported( String [ ] tokenEndpointAuthSigningAlgValuesSupported )
    {
        this._tokenEndpointAuthSigningAlgValuesSupported = tokenEndpointAuthSigningAlgValuesSupported;
    }

    public String [ ] getDisplayValuesSupported( )
    {
        return _displayValuesSupported;
    }

    @JsonProperty( "display_values_supported" )
    public void setDisplayValuesSupported( String [ ] displayValuesSupported )
    {
        this._displayValuesSupported = displayValuesSupported;
    }

    public String [ ] getClaimTypesSupported( )
    {
        return _claimTypesSupported;
    }

    @JsonProperty( "claim_types_supported" )
    public void setClaimTypesSupported( String [ ] claimTypesSupported )
    {
        this._claimTypesSupported = claimTypesSupported;
    }

    public String [ ] getClaimsSupported( )
    {
        return _claimsSupported;
    }

    @JsonProperty( "claims_supported" )
    public void setClaimsSupported( String [ ] claimsSupported )
    {
        this._claimsSupported = claimsSupported;
    }

    public String getServiceDocumentation( )
    {
        return _serviceDocumentation;
    }

    @JsonProperty( "service_documentation" )
    public void setServiceDocumentation( String serviceDocumentation )
    {
        this._serviceDocumentation = serviceDocumentation;
    }

    public String [ ] getClaimsLocalesSupported( )
    {
        return _claimsLocalesSupported;
    }

    @JsonProperty( "claims_locales_supported" )
    public void setClaimsLocalesSupported( String [ ] claimsLocalesSupported )
    {
        this._claimsLocalesSupported = claimsLocalesSupported;
    }

    public String [ ] getUILocalesSupported( )
    {
        return _uiLocalesSupported;
    }

    @JsonProperty( "ui_locales_supported" )
    public void setUILocalesSupported( String [ ] uiLocalesSupported )
    {
        this._uiLocalesSupported = uiLocalesSupported;
    }

    public boolean isClaimsParameterSupported( )
    {
        return _claimsParameterSupported;
    }

    @JsonProperty( "claims_parameter_supported" )
    public void setClaimsParameterSupported( boolean claimsParameterSupported )
    {
        this._claimsParameterSupported = claimsParameterSupported;
    }

    public boolean isRequestParameterSupported( )
    {
        return _requestParameterSupported;
    }

    @JsonProperty( "request_parameter_supported" )
    public void setRequestParameterSupported( boolean requestParameterSupported )
    {
        this._requestParameterSupported = requestParameterSupported;
    }

    public boolean isRequestUIRParameterSupported( )
    {
        return _requestUIRParameterSupported;
    }

    @JsonProperty( "request_uri_parameter_supported" )
    public void setRequestUIRParameterSupported( boolean requestUIRParameterSupported )
    {
        this._requestUIRParameterSupported = requestUIRParameterSupported;
    }

    public boolean isRequireRequestUIRRegistration( )
    {
        return _requireRequestUIRRegistration;
    }

    @JsonProperty( "require_request_uri_registration" )
    public void setRequireRequestUIRRegistration( boolean requireRequestUIRRegistration )
    {
        this._requireRequestUIRRegistration = requireRequestUIRRegistration;
    }

    public String getOpPolicyURI( )
    {
        return _opPolicyURI;
    }

    @JsonProperty( "op_policy_uri" )
    public void setOpPolicyURI( String opPolicyURI )
    {
        this._opPolicyURI = opPolicyURI;
    }

    public String getOpTOSURI( )
    {
        return _opTOSURI;
    }

    @JsonProperty( "op_tos_uri" )
    public void setOpTOSURI( String opTOSURI )
    {
        this._opTOSURI = opTOSURI;
    }

    public String getEndSessionEndpoint( )
    {
        return _strEndSessionEndpoint;
    }

    @JsonProperty( "end_session_endpoint" )
    public void setEndSessionEndpoint( String strEndSessionEndpoint )
    {
        this._strEndSessionEndpoint = strEndSessionEndpoint;
    }

    /**
     * Validate the configuration.
     * 
     * @param strExpectedIssuer
     *            the expected issuer
     * @throws NullPointerException
     *             if a required parameter is absent
     * @throws AppException
     *             if another error is present
     */
    public void validate( String strExpectedIssuer )
    {
        validateIssuer( strExpectedIssuer );
        validateAuthorizationEndpoint( );
        validateTokenEndpoint( );
        validateUserinfoEndpoint( );
        validateJwksURI( );
        validateRegistrationEndpoint( );
        validateScopesSupported( );
        validateResponseTypeSupported( );
        validateSubjectTypesSupported( );
        validateIDTokenSigningAlgValuesSupported( );
        validateTokenEndpointAuthSigningAlgValuesSupported( );
        validateEndSessionEndpoint( );
    }

    private void validateIssuer( String strExpectedIssuer )
    {
        Objects.requireNonNull( _strIssuer, "issuer is required" );
        validateURI( _strIssuer, "issuer", false, false );
        if ( !_strIssuer.equals( strExpectedIssuer ) )
        {
            throw new AppException( "Expected issuer " + strExpectedIssuer + ", but got " + _strIssuer );
        }
    }

    private void validateAuthorizationEndpoint( )
    {
        Objects.requireNonNull( _strAuthorizationEndpoint, "Authorization endpoint is required" );
        validateURI( _strAuthorizationEndpoint, "Authorization endpoint" );
    }

    private void validateTokenEndpoint( )
    {
        if ( _strTokenEndpoint == null )
        {
            // FIXME This is REQUIRED unless only the Implicit Flow is used
            return;
        }
        validateURI( _strTokenEndpoint, "Token endpoint" );
    }

    private void validateUserinfoEndpoint( )
    {
        if ( _strUserinfoEndpoint == null )
        {
            return;
        }
        validateURI( _strUserinfoEndpoint, "Userinfo endpoint" );
    }

    private void validateJwksURI( )
    {
        Objects.requireNonNull( _strJwksURI, "JWKS URI is required" );
        validateURI( _strJwksURI, "JWKS URI" );
    }

    private void validateRegistrationEndpoint( )
    {
        if ( _strRegistrationEndpoint == null )
        {
            return;
        }
        validateURI( _strRegistrationEndpoint, "Registration endpoint" );
    }

    private void validateScopesSupported( )
    {
        // The server MUST support the openid scope value. Servers MAY choose not to advertise some supported scope values even when this parameter is used,
        // although those defined in [OpenID.Core] SHOULD be listed, if supported.
    }

    private void validateResponseTypeSupported( )
    {
        Objects.requireNonNull( _responseTypesSupported, "Response types supported is required" );
        for ( String type : _responseTypesSupported )
        {
            Objects.requireNonNull( type, "response type must not be null" );
        }
    }

    private void validateSubjectTypesSupported( )
    {
        Objects.requireNonNull( _subjectTypesSupported, "Subject types supported is required" );
        for ( String type : _subjectTypesSupported )
        {
            Objects.requireNonNull( type, "subject type must not be null" );
        }
    }

    private void validateTokenEndpointAuthSigningAlgValuesSupported( )
    {
        if ( _tokenEndpointAuthSigningAlgValuesSupported == null )
        {
            return;
        }
        if ( Arrays.stream( _tokenEndpointAuthSigningAlgValuesSupported ).anyMatch( alg -> "none".equals( alg ) ) )
        {
            throw new AppException( "The algorithm none MUST NOT be used for the token endpoint auth signing alg values supported" );
        }
    }

    private void validateEndSessionEndpoint( )
    {
        if ( _strEndSessionEndpoint == null )
        {
            return;
        }
        validateURI( _strEndSessionEndpoint, "end session endpoint" );
    }

    private void validateIDTokenSigningAlgValuesSupported( )
    {
        Objects.requireNonNull( _idTokenSigningAlgValuesSupported, "ID Token signing alg values supported is required" );
        if ( Arrays.stream( _idTokenSigningAlgValuesSupported ).noneMatch( alg -> "RS256".equals( alg ) ) )
        {
            throw new AppException( "The algorithm RS256 MUST be included in ID Token signing alg values supported" );
        }
    }

    private void validateURI( String strURI, String strFieldName )
    {
        validateURI( strURI, strFieldName, true, true );
    }

    private void validateURI( String strURI, String strFieldName, boolean queryAllowed, boolean fragmentAllowed )
    {
        try
        {
            URI theURI = new URI( strURI );
            if ( !theURI.getScheme( ).equals( "https" ) )
            {
                throw new AppException( strFieldName + " must be a URL using the https scheme" );
            }
            if ( !queryAllowed && theURI.getQuery( ) != null )
            {
                throw new AppException( strFieldName + " must be a URL with no query component" );
            }
            if ( !fragmentAllowed && theURI.getFragment( ) != null )
            {
                throw new AppException( strFieldName + " must be a URL with no fragment  component" );
            }
        }
        catch( URISyntaxException e )
        {
            throw new AppException( "Unable to validate URI <" + strURI + "> for field " + strFieldName + ": " + e.getMessage( ), e );
        }
    }
}
