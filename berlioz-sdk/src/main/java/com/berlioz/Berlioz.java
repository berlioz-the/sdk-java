package com.berlioz;


import com.berlioz.agent.Client;

public class Berlioz {

    public static void DoSomething() {
        System.out.println("BERLIOZ:: DO SOMETHING");

    }

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
        System.out.println("BERLIOZ:: RUN");
        Client client = new Client();
        client.run();
    }
}
