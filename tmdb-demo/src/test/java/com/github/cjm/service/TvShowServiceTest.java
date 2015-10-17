/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cjm.service;

import com.github.cjm.resource.TvShow;
import com.github.cjm.resource.TvShowCollection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author moscac
 */
public class TvShowServiceTest {
    
    public TvShowServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testLoad() {
        TvShowService instance = new TvShowService();
        TvShowCollection result = instance.load(TvShowService.RESOURCE_TV_POPULAR);
        Assert.assertNotNull(result);
        for (TvShow tvShow : result.getResults()) {
            System.out.println(tvShow.getName());
        }
    }
    
}
