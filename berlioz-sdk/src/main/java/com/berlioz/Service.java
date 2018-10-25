package com.berlioz;

import com.berlioz.agent.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public void all() {
        logger.info("Service {} :: ALL", this._id);
    }
}
