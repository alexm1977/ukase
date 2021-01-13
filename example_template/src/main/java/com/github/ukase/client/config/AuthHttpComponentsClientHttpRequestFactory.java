package com.github.ukase.client.config;


import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;

/**
 * Фабрика по созданию HTTP запроса на базе Apache HTTP клиента с возможностью
 * проставления логина и пароля базовой аутентификации.
 *
 * @author Victor Svirkin
 * @version 1.0 - 02.12.14
 */
public class AuthHttpComponentsClientHttpRequestFactory
        extends HttpComponentsClientHttpRequestFactory {

    protected String userName;

    protected String password;

    public AuthHttpComponentsClientHttpRequestFactory() {
        // Empty
    }

    public AuthHttpComponentsClientHttpRequestFactory(String userName, String password) {
        super();
        this.userName = userName;
        this.password = password;
    }

    public AuthHttpComponentsClientHttpRequestFactory(HttpClient httpClient) {
        this(httpClient, null, null);
    }

    public AuthHttpComponentsClientHttpRequestFactory(HttpClient httpClient, String userName, String password) {
        super(httpClient);
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();

        HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        authCache.put(host, basicAuth);

        // Add AuthCache to the execution context
        HttpClientContext localcontext = HttpClientContext.create();
        localcontext.setAuthCache(authCache);

        if (userName != null) {
            BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(host),
                    new UsernamePasswordCredentials(userName, password)
            );
            localcontext.setCredentialsProvider(credsProvider);
        }

        return localcontext;
    }
}
