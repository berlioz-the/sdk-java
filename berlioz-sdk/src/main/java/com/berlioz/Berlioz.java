package com.berlioz;


import com.berlioz.agent.Client;
import com.berlioz.comm.Policy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Berlioz {
    private static Logger logger = LogManager.getLogger(Berlioz.class);
    private static Registry registry = new Registry();
    private static Processor processor = new Processor(registry);

    public static Sector sector(String name) {
        return new Sector(name);
    }


    public static Service service(String name) {
        return service(name, null);
    }

    public static Service service(String name, String endpoint) {
        return sector(System.getenv("BERLIOZ_SECTOR")).service(name, endpoint);
    }

    public static void run() {
        logger.info("Run...");
        Client client = new Client(processor);
        client.run();
        
        registry.subscribe("policies", ListHelper.Path(), new Registry.Callback<Policy>() {
            public void callback(Policy value) {
                logger.info("POLICIES CHANGED. VALUE: {}", value);
            }
        });
    }
}
