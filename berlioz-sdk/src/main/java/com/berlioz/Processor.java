package com.berlioz;

import com.berlioz.comm.BasePeerData;
import com.berlioz.comm.Endpoint;
import com.berlioz.comm.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Processor {
    private static Logger logger = LogManager.getLogger(Berlioz.class);
    private Parser _parser = new Parser();
    private Registry _registry;

    Processor(Registry registry)
    {
        _registry = registry;
    }

    public void accept(String rawMessage)
    {
        logger.info("Raw message: {}", rawMessage);
        Message message = _parser.parse(rawMessage);

        logger.info("Parsed message: {}", message);
        logger.info("Parsed message to JSON: {}", _parser.toJson(message));

        this._acceptPolicies(message);
        this._acceptEndpoints(message);
        this._acceptPeers(message);
    }

    private void _acceptPolicies(Message message)
    {
        if (message.policies == null) {
            // this._registry.reset("policies", ListHelper.Path());
            return;
        }
        this._registry.set("policies", ListHelper.Path(), message.policies);
    }

    private void _acceptEndpoints(Message message)
    {
        if (message.policies == null) {
            // this._registry.reset("endpoints", ListHelper.Path());
            return;
        }
        this._registry.set("endpoints", ListHelper.Path(), message.endpoints);
        for(String name: message.endpoints.keySet()) {
            Endpoint endpoint = message.endpoints.get(name);
            this._registry.set("endpoints", ListHelper.Path(name), endpoint);
        }
    }

    private void _acceptPeers(Message message)
    {
        if (message.peers == null) {
            return;
        }
        this._registry.set("byService", ListHelper.Path(), message.peers);
        for(String id: message.peers.byService.keySet()) {
            BasePeerData data = message.peers.byService.get(id);
            this._registry.set("endpoints", ListHelper.Path(id), data);
        }
    }
}
