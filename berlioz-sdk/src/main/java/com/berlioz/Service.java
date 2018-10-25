package com.berlioz;

import com.berlioz.agent.Client;
import com.berlioz.comm.Endpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class Service {
    private static Logger logger = LogManager.getLogger(Service.class);

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

    public void monitorAll(Registry.Callback<Map<String, Endpoint>> cb)
    {

    }

    public void monitorFirst(Registry.Callback<Endpoint> cb)
    {

    }

    public Map<String, Endpoint> all()
    {
        return null;
    }

    public Endpoint first()
    {
        return null;
    }

    public Endpoint random()
    {
        return null;
    }
}
