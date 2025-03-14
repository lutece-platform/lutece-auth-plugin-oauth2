package fr.paris.lutece.plugins.oauth2.service;

import org.apache.hc.client5.http.impl.cache.CachingHttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import fr.paris.lutece.util.httpaccess.HttpAccessService;
import fr.paris.lutece.util.httpaccess.HttpClientConfiguration;

public class CachingHttpAccessService extends HttpAccessService
{

    public CachingHttpAccessService( HttpClientConfiguration httpClientConfiguration )
    {
        super( httpClientConfiguration );
    }

    @Override
    protected HttpClientBuilder getHttpClientBuilder( )
    {
        return CachingHttpClientBuilder.create();
    }

}
