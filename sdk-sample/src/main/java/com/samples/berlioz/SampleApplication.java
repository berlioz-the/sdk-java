package com.samples.berlioz;

import com.berlioz.Berlioz;

import java.util.Map;
import java.util.Scanner;

import com.berlioz.Registry;
import com.berlioz.comm.Endpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SampleApplication
{
    private static Logger logger = LogManager.getLogger(SampleApplication.class);

    public static void main(String args[])
    {
        logger.info("Hello World!!");

        Berlioz.run();

        logger.info("ALL APP DEFAULTS: {}", Berlioz.service("app").all());
        logger.info("FIRST APP DEFAULT: {}", Berlioz.service("app").first());
        logger.info("RANDOM APP DEFAULT: {}", Berlioz.service("app").random());


        Berlioz.service("app").monitorAll(new Registry.Callback<Map<String, Endpoint>>() {
            public void callback(Map<String, Endpoint> value) {
                logger.info("MONITOR APP DEFAULTS: {}", value);
                logger.info("MONITOR APP DEFAULTS XX: {}", Berlioz.service("app").all());
            }
        });

        Berlioz.service("app").monitorFirst(new Registry.Callback<Endpoint>() {
            public void callback(Endpoint value) {
                logger.info("MONITOR APP FIRST: {}", value);
                logger.info("MONITOR APP FIRST XX: {}", Berlioz.service("app").first());
                logger.info("MONITOR APP RANDOM XX: {}", Berlioz.service("app").random());
            }
        });


        new Scanner(System.in).nextLine(); // Don't close immediately.
    }
}
