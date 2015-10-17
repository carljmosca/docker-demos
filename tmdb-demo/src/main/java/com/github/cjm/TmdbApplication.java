/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cjm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author moscac
 */

@EnableAutoConfiguration
@ComponentScan
public class TmdbApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmdbApplication.class, args);
    }

}