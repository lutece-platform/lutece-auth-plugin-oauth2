package fr.paris.lutece.plugins.oauth2.service;

import java.util.Map;

public class OauthUtils
{

    /**
     * Utils to trace map content
     * 
     * @param map
     *            The map
     * @return The content
     */
    public static String traceMap( Map<String, String> map )
    {
        StringBuilder sbTrace = new StringBuilder( );

        for ( Map.Entry entry : map.entrySet( ) )
        {
            sbTrace.append( entry.getKey( ) ).append( ":[" ).append( entry.getValue( ) ).append( "]\n" );
        }

        return sbTrace.toString( );
    }
}
