/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cjm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author moscac
 */
public class EnvironmentManager {

    private final static Logger log = LoggerFactory.getLogger(EnvironmentManager.class);
    private final static String[] REQUIRED_VARIABLES = {"MOVIEDB_API_KEY",
        "MYSQL_PORT_3306_TCP_ADDR", "MYSQL_PORT_3306_TCP_PORT", "MYSQL_ENV_MYSQL_ROOT_PASSWORD"};

    public static boolean checkEnvironment() {
        log.info("Checking required environment variables");
        boolean result = true;
        for (String s : REQUIRED_VARIABLES) {
            if (System.getenv(s) == null) {
                log.error(s + " environment variable must be defined");
                result = false;
            }
        }
        return result;
    }

}
