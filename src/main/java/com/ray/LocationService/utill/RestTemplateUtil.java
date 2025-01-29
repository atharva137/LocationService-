package com.ray.LocationService.utill;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RestTemplateUtil {



    private final RestTemplate restTemplate;


    @Autowired
    public RestTemplateUtil(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }


    public <T> T makeRequest(String url, HttpMethod method, Object requestBody, Map<String,String> headers, Class<T> responseType){
        HttpHeaders httpHeaders = new HttpHeaders();

        if(headers!=null){
            headers.forEach(httpHeaders::add);
        }

        HttpEntity<Object> entity = new HttpEntity<>(requestBody, httpHeaders);

        ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, entity, responseType);
        return responseEntity.getBody();
    }


    public <T> T get(String url,  Object requestBody,Map<String,String> headers ,Class<T> responseType){
        return makeRequest(url,HttpMethod.GET,requestBody, headers, responseType);
    }

    public <T> T post(String url , Object requestBody, Map<String, String> headers , Class<T> responseType){
        return makeRequest(url,HttpMethod.POST,requestBody,headers,responseType);
    }
    public <T> T put(String url, Object requestBody, Map<String, String> headers, Class<T> responseType) {
        return makeRequest(url, HttpMethod.PUT, requestBody, headers, responseType);
    }

    // Generic DELETE request
    public <T> T delete(String url, Map<String, String> headers, Class<T> responseType) {
        return makeRequest(url, HttpMethod.DELETE, null, headers, responseType);
    }





}
