package com.berlioz;

import com.berlioz.comm.*;
import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Parser {
    private static Logger logger = LogManager.getLogger(Parser.class);

    public Message parse(String rawMessage)
    {
        Message message = this.getGson().fromJson(rawMessage, Message.class);
        return message;
    }

    public String toJson(Message message)
    {
        return this.getGson().toJson(message);
    }

    public class PeersAdapter implements JsonDeserializer<Peers> {

        public Peers deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final JsonObject peersObject = json.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entries = peersObject.entrySet();
            Peers peers = new Peers();
            peers.peers = new HashMap<String, BasePeerData>();
            for (Map.Entry<String, JsonElement> entry: entries) {
                BasePeerData peerData = this.parseService(entry.getKey(), entry.getValue(), context);
                if (peerData != null) {
                    peers.peers.put(entry.getKey(), peerData);
                }
            }
            return peers;
        }

        private BasePeerData parseService(String id, JsonElement json, JsonDeserializationContext context)
        {
            if (id.startsWith("service://") || id.startsWith("cluster://")) {
                return this.parseEndpointService(json, context);
            } else {
                return this.parseNativeService(json);
            }
        }

        private BasePeerData parseEndpointService(JsonElement json, JsonDeserializationContext context)
        {
            final JsonObject jsonObject = json.getAsJsonObject();

            ServicePeerData serviceData = new ServicePeerData();
            serviceData.peers = new HashMap<String, Map<String, Endpoint>>();

            Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
            for (Map.Entry<String, JsonElement> entry : entries)
            {
                Map<String, Endpoint> endpoints = this.parseEndpoints(entry.getValue(), context);
                if (endpoints != null) {
                    serviceData.peers.put(entry.getKey(), endpoints);
                }
            }

            return serviceData;
        }

        private Map<String, Endpoint> parseEndpoints(JsonElement json, JsonDeserializationContext context)
        {
            Map<String, Endpoint> map = new HashMap<String, Endpoint>();

            final JsonObject jsonObject = json.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
            for (Map.Entry<String, JsonElement> entry : entries)
            {
                Endpoint endpointObj = context.deserialize(entry.getValue(), Endpoint.class);
                if (endpointObj != null) {
                    map.put(entry.getKey(), endpointObj);
                }
            }

            return map;
        }

        private BasePeerData parseNativeService(JsonElement json)
        {
            return new BasePeerData();
        }
    }

    private Gson getGson()
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Peers.class, new PeersAdapter());
        Gson gson = builder.setPrettyPrinting().create();
        return gson;
    }
}