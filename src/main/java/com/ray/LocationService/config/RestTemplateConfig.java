package com.ray.LocationService.config;

import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.config.RequestConfig;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {

    @Value("${http.connect.timeout:10000}")
    private int connectTimeout;

    @Value("${http.socket.timeout:30000}")
    private int socketTimeout;

    @Value("${http.connection.request.timeout:5000}")
    private int connectionRequestTimeout;

    @Bean
    public RestTemplate restTemplate() {
        // Create a PoolingHttpClientConnectionManager for connection pooling
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);  // Set max total connections
        connectionManager.setDefaultMaxPerRoute(50);  // Max connections per route

        // Create a RequestConfig instance to set timeouts
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(connectTimeout))
                .setResponseTimeout(Timeout.ofMilliseconds(socketTimeout))
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(connectionRequestTimeout))
                .build();

        // Create the HttpClient
        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        // Create HttpComponentsClientHttpRequestFactory using HttpClient
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(closeableHttpClient);

        // Return RestTemplate instance
        return new RestTemplate(factory);
    }
}
