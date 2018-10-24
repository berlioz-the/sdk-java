package com.berlioz;

public class Service {
    String _id;
    String _endpoint;


    Service(String id)
    {
        this._id = id;
    }

    Service(String id, String endpoint)
    {
        this(id);
        this._endpoint = endpoint;
    }

    public void all() {
        System.out.printf("Service %s :: ALL", this._id);
    }
}
