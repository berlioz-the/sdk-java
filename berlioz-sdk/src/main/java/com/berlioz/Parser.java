package com.berlioz;

import com.berlioz.msg.*;
import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Parser {
    private static Logger logger = LogManager.getLogger(Parser.class);

    public boolean isEndpointService(String id)
    {
        return id.startsWith("service://") || id.startsWith("cluster://");
    }

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
            peers.byService = new HashMap<String, PeerData>();
            for (Map.Entry<String, JsonElement> entry: entries) {
                PeerData peerData = this.parseService(entry.getKey(), entry.getValue(), context);
                if (peerData != null) {
                    peers.byService.put(entry.getKey(), peerData);
                }
            }
            return peers;
        }

        private PeerData parseService(String id, JsonElement json, JsonDeserializationContext context)
        {
            if (isEndpointService(id)) {
                return this.parseEndpointService(json, context);
            } else {
                return this.parseNativeService(json, context);
            }
        }

        private PeerData parseEndpointService(JsonElement json, JsonDeserializationContext context)
        {
            final JsonObject jsonObject = json.getAsJsonObject();

            PeerData serviceData = new PeerData();
            serviceData.peers = new HashMap<>();

            Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
            for (Map.Entry<String, JsonElement> entry : entries)
            {
                Endpoint endpointObj = context.deserialize(entry.getValue(), Endpoint.class);
                if (endpointObj != null) {
                    serviceData.peers.put(entry.getKey(), endpointObj);
                }
            }
            return serviceData;
        }

        private PeerData parseNativeService(JsonElement json, JsonDeserializationContext context)
        {
            final JsonObject jsonObject = json.getAsJsonObject();

            PeerData serviceData = new PeerData();
            serviceData.peers = new HashMap<>();

            Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
            for (Map.Entry<String, JsonElement> entry : entries)
            {
                NativeEndpoint endpointObj = context.deserialize(entry.getValue(), NativeEndpoint.class);
                if (endpointObj != null) {
                    serviceData.peers .put(entry.getKey(), endpointObj);
                }
            }
            return serviceData;
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
