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

# Installation

## Configuration

Configurer le fichier de context du plugin (WEB-INF/conf/plugins/oauth2_context.xml).

Il faut notamment paramétrer :
 
* Les adresses des WebServices la plate-forme Oauth2 cible (end points)
* Vos identifiants (id, secret) qui vous auront été fournit par le service oauth2 utilisé
* Si le serveur utilise JWT et que les tokens sont signés, alors le paramètre signatureAlgorithmName soit être renseigné. Si les tokens nesont pas signés, alors le paramètre signatureAlgorithmName ne doit pas être renseigné
* Si le paramètre jwksEndpointUri est renseigné, les clefs de signature des token sont téléchargées depuis cette adresse, qui doit pointer vers un fichier JWKS.
* L'adresse du Callback du plugin (NB : Cette adresse doit être enregistrée et associée à votre ID Client auprès du service Oauth2 utilisé.
doit ensuite être paramétré avec les informationsdu service client (id, secret et callback) :


```
           
     
    <bean id="oauth2.server" class="fr.paris.lutece.plugins.oauth2.business.AuthServerConf">
        <property name="issuer" value=" **** à renseigner **** "/>
        <property name="authorizationEndpointUri"
                                  value=" **** à renseigner **** "/>
        <property name="tokenEndpointUri" value=" **** à renseigner **** "/>
        <property name="logoutEndpointUri" value=" **** à renseigner **** "/>
        <property name="enableJwtParser" value="****true si le serveur utilise JWT ****" />
        <property name="signatureAlgorithmName" value="HS512"/>
        <property name="jwksEndpointUri" value=" **** à renseigner **** "/>
    </bean> 
    
    <bean id="oauth2.client" class="fr.paris.lutece.plugins.oauth2.business.AuthClientConf">
        <property name="clientId" value=" **** à renseigner **** "/>
        <property name="clientSecret" value=" **** à renseigner **** "/>
        <property name="redirectUri" value=" **** à renseigner **** "/>
    </bean>       
    
    <bean id="oauth2.callbackHandler" class="fr.paris.lutece.plugins.oauth2.web.CallbackHandler" >
      ;<property name="authServerConf" ref="oauth2.server">
       <property name="authClientConf" ref="oauth2.client">
       <property name="jWTParser" ref="oauth2.jWTParser"">
    </bean>      
    
    <!-- DataClient UserInfo -->
   <bean id="oauth2.logUserInfoDataClient" class="fr.paris.lutece.plugins.oauth2.dataclient.LogUserInfoDataClient">
         <property name="name" value="logUserInfo" />
         <property name="dataServerUri" value="https://fcp.integ01.dev-oauth2.fr/api/v1/userinfo"/>
         <property name="enableJwtParser" value="****true si le serveur utilise JWT ****" />
         <property name="tokenMethod" value="HEADER"/>
         <property name="scope">
             <set value-type="java.lang.String">
                 <value>openid </value>
                 <value>profile </value>
                 <value>email </value>
                 <value>address </value>
                 <value>phone </value>
             </set>
         </property>
         <!-- Optional eIDAS management -->
         <!--
         <property name="acrValuesSet">
             <set value-type="java.lang.String">
                 <value>eidas2 </value>
             </set>
         </property>
        -->
     </bean>

     <!--     <bean id="oauth2.jwtParser" class="fr.paris.lutece.plugins.oauth2.oidc.jwt.MitreJWTParser" /> -->
     <bean id="oauth2.jwtParser" class="fr.paris.lutece.plugins.oauth2.jwt.JjwtJWTParser" />
        


```


## Dépannage


 
* L'activation des logs en mode debug se fait en ajoutant la ligne suivante dans le fichier `WEB-INF/conf/config.properties` dans la rubrique LOGGERS :

```

							log4j.logger.lutece.oauth2=DEBUG, Console
							
```





[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/plugin-oauth2/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*