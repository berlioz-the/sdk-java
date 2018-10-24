package com.berlioz;

public class Sector {
    String _name;

    Sector(String name)
    {
        this._name = name;
    }

    public Service service(String name, String endpoint) {
        String id = String.format("service://%s-%s-%s", System.getenv("BERLIOZ_CLUSTER"), this._name, name);
        return new Service(id, endpoint);
    }
}
