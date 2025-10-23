package fr.paris.lutece.plugins.oauth2.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.paris.lutece.plugins.oauth2.business.AuthClientConf;
import fr.paris.lutece.plugins.oauth2.business.AuthServerConf;
import fr.paris.lutece.plugins.oauth2.jwt.JWTParser;
import fr.paris.lutece.plugins.oauth2.web.CallbackHandler;
import fr.paris.lutece.portal.service.util.CdiHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Optional;


@ApplicationScoped
public class Oauth2CallbackHandlerBeansProducer
{

    @Inject
    private DataClientService _dataClientService;
    @Inject
    private TokenService _tokenService;
    @Inject
    @Named("oauth2.client")
    private AuthClientConf _authClientConf;
    /**
     * Produces the OAuth2 CallbackHandler bean
     * 
     * @param oauth2CallbackHandlerServer The OAuth2 server name
     * @param oauth2JwtParser The JWT parser name
     * @param isDefault True if this is the default handler
     * @return The CallbackHandler instance
     */

    @Produces
    @ApplicationScoped
    @Named( "oauth2.callbackHandler" )
    public CallbackHandler produceOauth2CallbackHandler(
        @ConfigProperty( name="oauth2.callbackHandler.server" ) Optional<String> oauth2CallbackHandlerServer,
        @ConfigProperty( name = "oauth2.callbackHandler.jWTParser" ) Optional<String> oauth2JwtParser,
        @ConfigProperty( name = "oauth2.callbackHandler.default", defaultValue = "true" ) boolean isDefault )
    {
        return new CallbackHandler(
            "oauth2.callbackHandler",
            CdiHelper.getReference( AuthServerConf.class, oauth2CallbackHandlerServer.orElse( "oauth2.oIDCServer" ) ),
            _authClientConf,
            CdiHelper.getReference( JWTParser.class, oauth2JwtParser.orElse( "oauth2.jjwtJWTParser" ) ),
            isDefault,
            _dataClientService,
            _tokenService
        );
    
    }
}
