package com.berlioz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Service extends BaseService  {
    private static Logger logger = LogManager.getLogger(Service.class);

    Service(String id, String endpoint)
    {
        super(id, endpoint);
    }
}
