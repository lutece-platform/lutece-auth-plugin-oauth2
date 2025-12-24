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

## Configuration Properties (oauth2.properties)

Les paramètres de configuration du plugin peuvent être externalisés dans le fichier `WEB-INF/conf/plugins/oauth2.properties` 
Configuration Client OAuth2
| Paramètre| Description| Valeur par défaut|
|-----------------|-----------------|-----------------|
|  `oauth2.client.clientId` | Identifiant du client enregistré auprès du fournisseur OAuth2| <obligatoire>|
|  `oauth2.client.clientSecret` | Secret du client pour l'authentification auprès du serveur OAuth2| <obligatoire>|
|  `oauth2.client.public` | Indique si le client est public (sans secret)| false|
|  `oauth2.client.pkce` | Active le support PKCE (Proof Key for Code Exchange) pour les clients publics| false|
|  `oauth2.client.redirectUri` | URI de redirection après authentification. si vide servlet/plugins/oauth2/callback| |
|  `oauth2.client.postLogoutRedirectUri` | URI de redirection après déconnexion. si vide page d'accueil Lutece| |
Configuration du Serveur OAuth2 (OpenID Connect)
| Paramètre| Description|
|-----------------|-----------------|
|  `oauth2.oIDCServer.issuer` | Identifiant du fournisseur d'identités (issuer) pour OpenID Connect. Utilisé pour découvrir automatiquement les endpoints du serveur OAuth2|
Configuration du Serveur OAuth2 (Directe)
| Paramètre| Description|
|-----------------|-----------------|
|  `oauth2.server.issuer` | Identifiant unique du fournisseur d'identités|
|  `oauth2.server.authorizationEndpointUri` | URI du endpoint d'autorisation du serveur OAuth2|
|  `oauth2.server.tokenEndpointUri` | URI du endpoint de tokens du serveur OAuth2|
|  `oauth2.server.logoutEndpointUri` | URI du endpoint de déconnexion (logout) du serveur OAuth2|
|  `oauth2.server.enableJwtParser` | Active le parsing des JWT si le serveur utilise des tokens signés|
|  `oauth2.server.iDTokenSignatureAlgorithmNames` | Algorithmes de signature acceptés pour les ID tokens (ex: HS512,RS256,RS512,ES256,ES512)|
|  `oauth2.server.jwksEndpointUri` | URI du endpoint JWKS pour télécharger les clés de signature publiques|
Configuration du Callback Handler
| Paramètre| Description| Valeurs|
|-----------------|-----------------|-----------------|
|  `oauth2.callbackHandler.jwtParser` | Implémentation du parser JWT| oauth2.jjwtJWTParser ou oauth2.mitreJWTParser|
|  `oauth2.callbackHandler.default` | Indique si ce callback handler est celui par défaut| true / false|
|  `oauth2.callbackHandler.server` | Référence au bean de configuration du serveur OAuth2| oauth2.oIDCServer ou oauth2.server|
Configuration du DataClient LogUserInfo
| Paramètre| Description|
|-----------------|-----------------|
|  `oauth2.dataclient.logUserInfo.dataServerUri` | URI du endpoint userinfo du serveur OAuth2 (ex: https://your-server.com/api/v1/userinfo)|
|  `oauth2.dataclient.logUserInfo.tokenMethod` | Méthode de transmission du token (ex: HEADER pour Authorization header)|
|  `oauth2.dataclient.logUserInfo.scopes` | Scopes OAuth2 demandés (ex: openid,profile,email,address,phone)|
|  `oauth2.dataclient.logUserInfo.acrValues` | Paramètre optionnel pour eIDAS (ex: eidas2) - spécifie le niveau d'authentification requis|
Configuration Globale
| Paramètre| Description| Valeurs|
|-----------------|-----------------|-----------------|
|  `oauth2.jwtParser` | Implémentation du JWT Parser par défaut| jjwt ou mitre|
|  `oauth2.error.page` | Page JSP de gestion des erreurs OAuth2| jsp/site/Portal.jsp?page=oauth2HandleError&view=error|
Exemple de Configuration Complète
```

# Configuration Client OAuth2
oauth2.client.clientId=your-client-id
oauth2.client.clientSecret=your-client-secret
oauth2.client.public=false
oauth2.client.pkce=false
oauth2.client.redirectUri=
oauth2.client.postLogoutRedirectUri=

# Configuration Serveur OpenID Connect
oauth2.oIDCServer.issuer=https://your-oidc-server.com

# Configuration Callback Handler
oauth2.callbackHandler.jwtParser=oauth2.jjwtJWTParser
oauth2.callbackHandler.default=true
oauth2.callbackHandler.server=oauth2.oIDCServer

# Configuration DataClient LogUserInfo
oauth2.dataclient.logUserInfo.dataServerUri=https://your-oidc-server.com/api/v1/userinfo
oauth2.dataclient.logUserInfo.tokenMethod=HEADER
oauth2.dataclient.logUserInfo.scopes=openid,profile,email,address,phone

# Gestion des erreurs
oauth2.error.page=jsp/site/Portal.jsp?page=oauth2HandleError&view=error

# Implémentation JWT Parser
oauth2.jwtParser=jjwt

```
Recommandations
 
*  **Sécurité :** Les valeurs `clientId` et `clientSecret` ne doivent jamais être conservées en dur en environnement de production. Utilisez un système de gestion des secrets(variables d'environnement, vaults, fichiers sécurisés, etc.)
*  **OpenID Connect vs Configuration directe :** Si votre serveur OAuth2 supporte OpenID Connect, il est recommandé d'utiliser `oauth2.oIDCServer.issuer` pour une configuration plus simple et automatique. Sinon, utilisez la configuration directe avec `oauth2.server.*` 
*  **JWT Parser :** Le choix entre JJWT et MITRE dépend de votre implémentation. JJWT est plus léger, tandis que Nimbus JOSE + JWT offre plus de fonctionnalités
*  **PKCE :** Le support PKCE est recommandé pour les applications mobiles et les clients publics
*  **Scopes :** Adaptez la liste des scopes en fonction des informations dont vous avez besoin du serveur OAuth2

## Dépannage


 
* L'activation des logs en mode debug se fait en ajoutant la ligne suivante dans le fichier `WEB-INF/conf/config.properties` dans la rubrique LOGGERS :

```

                            log4j.logger.lutece.oauth2=DEBUG, Console
                            
```





[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/plugin-oauth2/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*