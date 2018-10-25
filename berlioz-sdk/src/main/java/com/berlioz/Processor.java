package com.berlioz;

import com.berlioz.comm.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Processor {
    private static Logger logger = LogManager.getLogger(Berlioz.class);
    private Parser _parser = new Parser();

    public void accept(String rawMessage)
    {
        logger.info("Raw message: {}", rawMessage);
        Message message = _parser.parse(rawMessage);

        logger.info("Parsed message: {}", message);

        logger.info("Parsed message to JSON: {}", _parser.toJson(message));
    }
}
