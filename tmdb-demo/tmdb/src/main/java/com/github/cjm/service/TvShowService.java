/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cjm.service;

import com.github.cjm.resource.TvShowCollection;
import org.springframework.stereotype.Service;

/**
 *
 * @author moscac
 */
@Service
public class TvShowService extends BaseService<TvShowCollection> {

    public static String RESOURCE_TV_POPULAR = "/tv/popular";
    
}
