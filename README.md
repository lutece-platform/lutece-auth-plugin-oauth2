![](https://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=auth-plugin-oauth2-deploy)
[![Alerte](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-oauth2&metric=alert_status)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-oauth2)
[![Line of code](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-oauth2&metric=ncloc)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-oauth2)
[![Coverage](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-oauth2&metric=coverage)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-oauth2)

# Plugin Oauth2

![](https://dev.lutece.paris.fr/plugins/plugin-oauth2/images/oauth2.png)

## Introduction

Ce plugin permet d'acceder à des ressources via le protocole oauth2. Grâce à l'authentification par le biais d'un fournisseur d'identités Oauth2,un fournisseur de service peut ensuite accéder à des ressources liées à l'utilisateur (et avec son consentement).

Ce plugin propose aux fournisseurs de service une API Java **DataClient** qui permet de créer des services d'accès aux données. Pour plus d'informations surl'utilisation de cette API, reportez-vous à la documentation du [Wiki](https://fr.lutece.paris.fr/) .

Ce plugin est également utilisé par le Module [MyLutece Oauth2](https://github.com/lutece-platform/lutece-auth-module-mylutece-oauth2) qui permet de faire une authentification Lutece basée sur le protocole oauth2.

# Configuration

## Properties Configuration (oauth2.properties)

Plugin configuration parameters can be externalized in the `WEB-INF/conf/plugins/oauth2.properties` file for better environment management.
OAuth2 Client Configuration
| Parameter| Description| Default Value|
|-----------------|-----------------|-----------------|
|  `oauth2.client.clientId` | Client identifier registered with the OAuth2 provider| <required>|
|  `oauth2.client.clientSecret` | Client secret for authentication with the OAuth2 server| <required>|
|  `oauth2.client.public` | Indicates if the client is public (without secret)| false|
|  `oauth2.client.pkce` | Enables PKCE support (Proof Key for Code Exchange) for public clients| false|
|  `oauth2.client.redirectUri` | Redirect URI after authentication. Default: servlet/plugins/oauth2/callback| servlet/plugins/oauth2/callback|
|  `oauth2.client.postLogoutRedirectUri` | Redirect URI after logout. Default: Lutece home page| Lutece home page|
OAuth2 Server Configuration (OpenID Connect)
| Parameter| Description|
|-----------------|-----------------|
|  `oauth2.oIDCServer.issuer` | Identity provider identifier (issuer) for OpenID Connect. Used to automatically discover OAuth2 server endpoints|
OAuth2 Server Configuration (Direct)
| Parameter| Description|
|-----------------|-----------------|
|  `oauth2.server.issuer` | Unique identifier of the identity provider|
|  `oauth2.server.authorizationEndpointUri` | URI of the authorization endpoint of the OAuth2 server|
|  `oauth2.server.tokenEndpointUri` | URI of the token endpoint of the OAuth2 server|
|  `oauth2.server.logoutEndpointUri` | URI of the logout endpoint of the OAuth2 server|
|  `oauth2.server.enableJwtParser` | Enables JWT parsing if the server uses signed tokens|
|  `oauth2.server.iDTokenSignatureAlgorithmNames` | Signature algorithms accepted for ID tokens (ex: HS512,RS256,RS512,ES256,ES512)|
|  `oauth2.server.jwksEndpointUri` | URI of the JWKS endpoint to download public signature keys|
Callback Handler Configuration
| Parameter| Description| Values|
|-----------------|-----------------|-----------------|
|  `oauth2.callbackHandler.jwtParser` | JWT parser implementation| oauth2.jjwtJWTParser or oauth2.mitreJWTParser|
|  `oauth2.callbackHandler.default` | Indicates if this callback handler is the default one| true / false|
|  `oauth2.callbackHandler.server` | Reference to the OAuth2 server configuration bean| oauth2.oIDCServer or oauth2.server|
LogUserInfo DataClient Configuration
| Parameter| Description|
|-----------------|-----------------|
|  `oauth2.dataclient.logUserInfo.dataServerUri` | URI of the userinfo endpoint of the OAuth2 server (ex: https://your-server.com/api/v1/userinfo)|
|  `oauth2.dataclient.logUserInfo.tokenMethod` | Token transmission method (ex: HEADER for Authorization header)|
|  `oauth2.dataclient.logUserInfo.scopes` | OAuth2 scopes requested (ex: openid,profile,email,address,phone)|
|  `oauth2.dataclient.logUserInfo.acrValues` | Optional parameter for eIDAS (ex: eidas2) - specifies the required authentication level|
Global Configuration
| Parameter| Description| Values|
|-----------------|-----------------|-----------------|
|  `oauth2.jwtParser` | Default JWT Parser implementation| jjwt or mitre|
|  `oauth2.error.page` | JSP page for OAuth2 error handling| jsp/site/Portal.jsp?page=oauth2HandleError&view=error|
Complete Configuration Example
```

# OAuth2 Client Configuration
oauth2.client.clientId=your-client-id
oauth2.client.clientSecret=your-client-secret
oauth2.client.public=false
oauth2.client.pkce=false
oauth2.client.redirectUri=
oauth2.client.postLogoutRedirectUri=

# OpenID Connect Server Configuration
oauth2.oIDCServer.issuer=https://your-oidc-server.com

# Callback Handler Configuration
oauth2.callbackHandler.jwtParser=oauth2.jjwtJWTParser
oauth2.callbackHandler.default=true
oauth2.callbackHandler.server=oauth2.oIDCServer

# DataClient LogUserInfo Configuration
oauth2.dataclient.logUserInfo.dataServerUri=https://your-oidc-server.com/api/v1/userinfo
oauth2.dataclient.logUserInfo.tokenMethod=HEADER
oauth2.dataclient.logUserInfo.scopes=openid,profile,email,address,phone

# Error handling
oauth2.error.page=jsp/site/Portal.jsp?page=oauth2HandleError&view=error

# JWT Parser Implementation
oauth2.jwtParser=jjwt

```
Recommendations
 
*  **Security:** The `clientId` and `clientSecret` values should never be hardcoded in production environments. Use a secrets management system(environment variables, vaults, secure files, etc.)
*  **OpenID Connect vs Direct Configuration:** If your OAuth2 server supports OpenID Connect, it is recommended to use `oauth2.oIDCServer.issuer` for simpler and automatic configuration. Otherwise, use direct configuration with `oauth2.server.*` 
*  **JWT Parser:** The choice between JJWT and MITRE depends on your implementation. JJWT is lighter, while Nimbus JOSE + JWT offers more features
*  **PKCE:** PKCE support is recommended for mobile applications and public clients
*  **Scopes:** Adjust the scope list according to the information you need from the OAuth2 server

## Dépannage


 
* L'activation des logs en mode debug se fait en ajoutant la ligne suivante dans le fichier `WEB-INF/conf/config.properties` dans la rubrique LOGGERS :

```

							log4j.logger.lutece.oauth2=DEBUG, Console
							
```





[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/plugin-oauth2/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*