package com.berlioz;


import com.berlioz.agent.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Berlioz {
    private static Logger logger = LogManager.getLogger(Berlioz.class);

    static Registry registry = new Registry();
    static PolicyResolver policy = new PolicyResolver(registry);
    private static Processor processor = new Processor(registry);
    static Zipkin zipkin = new Zipkin(policy);

    public static Sector sector(String name) {
        return new Sector(name);
    }

    public static Service service(String name) {
        return service(name, "default");
    }

    public static Service service(String name, String endpoint) {
        return sector(System.getenv("BERLIOZ_SECTOR")).service(name, endpoint);
    }

    public static void run() {
        logger.info("Run...");
        Client client = new Client(processor);
        client.run();
    }
}
