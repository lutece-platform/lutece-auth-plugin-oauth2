package fr.paris.lutece.plugins.oauth2.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import fr.paris.lutece.plugins.oauth2.web.CallbackHandler;
import fr.paris.lutece.plugins.oauth2.web.Constants;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.url.UrlItem;

/**
 * 
 * CallbackHandlerService
 *
 */
public class CallbackHandlerService
{

    private static CallbackHandlerService _singleton;
    private static Logger _logger = Logger.getLogger( Constants.LOGGER_OAUTH2 );

    /** Private constructor */
    private CallbackHandlerService( )
    {
    }

    /**
     * Return the unique instance
     * 
     * @return The unique instance
     */
    public static synchronized CallbackHandlerService instance( )
    {
        if ( _singleton == null )
        {
            _singleton = new CallbackHandlerService( );
        }

        return _singleton;
    }

    /**
     * Return Handler by name return the default handler if no name match
     * 
     * @param name
     *            the handler name
     * @return CallbackHandler
     */
    public CallbackHandler getCallbackHandler( String name )
    {

        CallbackHandler callbackHandler = null;

        List<CallbackHandler> callBackList = SpringContextService.getBeansOfType( CallbackHandler.class );

        if ( !StringUtils.isEmpty( name ) && callBackList.size( ) > 0 )
        {

            callbackHandler = callBackList.stream( ).filter( x -> name.equals( x.getHandlerName( ) ) ).findFirst( ).orElse( null );

        }

        // getDefaultHandler
        if ( callbackHandler == null )
        {

            callbackHandler = callBackList.stream( ).filter( x -> x.isDefault( ) ).findFirst( ).orElse( callBackList.stream( ).findFirst( ).orElse( null ) );

        }

        return callbackHandler;

    }

}
