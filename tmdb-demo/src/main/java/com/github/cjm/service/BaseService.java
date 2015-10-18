/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cjm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.cjm.resource.ResourceCollection;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author moscac
 * @param <T>
 */
public class BaseService<T extends ResourceCollection> {

    protected final String MOVIEDB_URL = "http://api.themoviedb.org/3";
    protected final String apiKey;
    private final int MAX_PAGES = 10;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public BaseService() {
        apiKey = System.getenv("MOVIEDB_API_KEY");
    }

    public ResourceCollection load(String resource, Class clazz, int page) {

        StringBuilder uri = new StringBuilder(MOVIEDB_URL);
        uri.append(resource);
        uri.append("?api_key=").append(apiKey);
        if (page > 1) {
            uri.append("&page=").append(page);
        }

        RestTemplate restTemplate = new RestTemplate();
        ResourceCollection<T> resourceCollection = new ResourceCollection<>();
        String data = null;
        try {
            data = restTemplate.getForObject(uri.toString(), String.class);
        } catch (HttpClientErrorException e) {
            log.warn(e.getMessage());
        }
        if (data != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                TypeFactory t = TypeFactory.defaultInstance();
                resourceCollection = mapper.readValue(data, t.constructType(clazz));
            } catch (IOException ex) {
                log.error("ObjectMapper exception: ", ex);
            }
        }
        return resourceCollection;
    }

    public ResourceCollection loadAll(String resource, Class clazz) {
        ResourceCollection<T> resourceCollection = new ResourceCollection<>();
        int page = 1;
        while (true) {
            ResourceCollection r = load(resource, clazz, page++);
            resourceCollection.getResults().addAll(r.getResults());
            if (page >= MAX_PAGES || resourceCollection.getResults().isEmpty()) {
                break;
            }
        }
        return resourceCollection;
    }
}
