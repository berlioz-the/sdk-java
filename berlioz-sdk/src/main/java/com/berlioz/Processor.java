package com.berlioz;

import com.berlioz.msg.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class Processor {
    private static Logger logger = LogManager.getLogger(Processor.class);
    private Parser _parser = new Parser();
    private Registry _registry;

    Processor(Registry registry)
    {
        _registry = registry;
    }

    public void accept(String rawMessage)
    {
        logger.trace("Raw message: {}", rawMessage);
        Message message = _parser.parse(rawMessage);

//        logger.trace("Parsed message to JSON: {}", _parser.toJson(message));

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
        if (message.endpoints == null) {
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
        for(String id: message.peers.byService.keySet()) {
            PeerData peerData = message.peers.byService.get(id);
            this._registry.set("peers", ListHelper.Path(id), peerData.peers);
        }
    }
}
