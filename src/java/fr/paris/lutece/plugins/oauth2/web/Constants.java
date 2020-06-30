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
package fr.paris.lutece.plugins.oauth2.web;

/**
 * Constants
 */
public final class Constants
{
    public static final String LOGGER_OAUTH2 = "lutece.oauth2";
    public static final String RESPONSE_TYPE_CODE = "code";
    public static final String PARAMETER_CODE = "code";
    public static final String PARAMETER_ERROR = "error";
    public static final String PARAMETER_SCOPE = "scope";
    public static final String PARAMETER_STATE = "state";
    public static final String PARAMETER_NONCE = "nonce";
    public static final String PARAMETER_REFRESH_TOKEN = "refresh_token";
    public static final String PARAMETER_GRANT_TYPE = "grant_type";
    public static final String PARAMETER_REDIRECT_URI = "redirect_uri";
    public static final String PARAMETER_CLIENT_ID = "client_id";
    public static final String PARAMETER_CLIENT_SECRET = "client_secret";
    public static final String PARAMETER_RESPONSE_TYPE = "response_type";
    public static final String PARAMETER_ACCESS_TOKEN = "access_token";
    public static final String PARAMETER_DATA_CLIENT = "data_client";
    public static final String PARAMETER_COMPLEMENTARY_PARAMETER = "complementary_parameter";

    public static final String PARAMETER_HANDLER_NAME = "handler_name";
    public static final String PARAMETER_ACR_VALUES = "acr_values";
    public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    public static final String STATE_SESSION_VARIABLE = "state";
    public static final String NONCE_SESSION_VARIABLE = "nonce";
    public static final String CLAIM_NONCE = "nonce";
    public static final String CLAIM_IDP = "idp";
    public static final String CLAIM_ACR = "acr";
    public static final String SESSION_ATTRIBUTE_DATACLIENT = "fc_data_client";
    public static final String PROPERTY_ERROR_PAGE = "oauth2.error.page";

    public static final String ERROR_TYPE_RETRIEVING_AUTHORIZATION_CODE = "ERROR_RETRIEVING_AN_AUTHORIZATION_CODE";
    public static final String ERROR_TYPE_INVALID_STATE = "ERROR_INVALID_STATE";
    public static final String CALL_BACK_SERVLET_URI = "servlet/plugins/oauth2/callback";

    /** Private constructor */
    private Constants( )
    {
    }
}
