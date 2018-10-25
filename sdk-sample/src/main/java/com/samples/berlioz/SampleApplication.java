package com.samples.berlioz;

import com.berlioz.Berlioz;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SampleApplication
{
    private static Logger logger = LogManager.getLogger(SampleApplication.class);

    public static void main(String args[])
    {
        logger.info("Hello World!!");

        Berlioz.run();

        Berlioz.service("web").all();

        new Scanner(System.in).nextLine(); // Don't close immediately.
    }
}
