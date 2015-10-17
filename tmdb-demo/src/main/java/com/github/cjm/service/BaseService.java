/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cjm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cjm.resource.ResourceCollection;
import com.github.cjm.resource.TvShowCollection;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author moscac
 * @param <T>
 */
public class BaseService<T> {

    protected final String MOVIEDB_URL = "http://api.themoviedb.org/3";
    protected final String apiKey;

    public BaseService() {
        apiKey = System.getenv("MOVIEDB_API_KEY");
    }

    public TvShowCollection load(String resource) {

        StringBuilder uri = new StringBuilder(MOVIEDB_URL);
        uri.append(resource);
        uri.append("?api_key=").append(apiKey);

        RestTemplate restTemplate = new RestTemplate();
        //ResourceCollection<T> resourceCollection = restTemplate.getForObject(uri.toString(), ResourceCollection.class);
        String data = restTemplate.getForObject(uri.toString(), String.class);
        ObjectMapper mapper = new ObjectMapper();
        TvShowCollection resourceCollection = null;
        //TypeFactory t = TypeFactory.defaultInstance();
        try {
            resourceCollection = mapper.readValue(data,  TvShowCollection.class);
        } catch (IOException ex) {
            Logger.getLogger(BaseService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resourceCollection;
    }

}
