package com.berlioz;

import com.berlioz.agent.Client;
import com.berlioz.comm.BaseEndpoint;
import com.berlioz.comm.Endpoint;
import com.berlioz.http.RestTemplate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Service {
    private static Logger logger = LogManager.getLogger(Service.class);

    String _id;
    String _endpoint;
    PeerAccessor _peerAccessor;

    Service(String id)
    {
        this._id = id;
    }

    Service(String id, String endpoint)
    {
        this(id);
        this._endpoint = endpoint;
        this._peerAccessor = new PeerAccessor(ListHelper.Path(this._id, this._endpoint));
    }

    public void monitorAll(final Registry.Callback<Map<String, Endpoint>> cb)
    {
        this._peerAccessor.monitorAll(new Registry.Callback<Map<String, BaseEndpoint>>() {
            public void callback(Map<String, BaseEndpoint> map) {
                Map<String, Endpoint> newMap = castPeers(map);
                cb.callback(newMap);
            }
        });
    }

    public void monitorFirst(final Registry.Callback<Endpoint> cb)
    {
        this._peerAccessor.monitorFirst(new Registry.Callback<BaseEndpoint>() {
            public void callback(BaseEndpoint ep) {
                cb.callback(castPeer(ep));
            }
        });
    }

    public Map<String, Endpoint> all()
    {
        Map<String, BaseEndpoint> map = this._peerAccessor.all();
        return castPeers(map);
    }

    public Endpoint first()
    {
        BaseEndpoint ep = this._peerAccessor.first();
        return castPeer(ep);
    }

    public Endpoint random()
    {
        BaseEndpoint ep = this._peerAccessor.random();
        return castPeer(ep);
    }

    public RestTemplate request()
    {
        RestTemplate restTemplate = new RestTemplate(this._peerAccessor);
        return restTemplate;
    }

    private Map<String, Endpoint> castPeers(Map<String, BaseEndpoint> map)
    {
        Map<String, Endpoint> newMap = new HashMap<String, Endpoint>();
        for(Map.Entry<String, BaseEndpoint> entry : map.entrySet()) {
            newMap.put(entry.getKey(), castPeer(entry.getValue()));
        }
        return newMap;
    }

    private Endpoint castPeer(BaseEndpoint peer)
    {
        return (Endpoint)peer;
    }
}
