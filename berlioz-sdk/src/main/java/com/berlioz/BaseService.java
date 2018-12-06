package com.berlioz;

import com.berlioz.msg.BaseEndpoint;
import com.berlioz.msg.Endpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class BaseService {
    private static Logger logger = LogManager.getLogger(BaseService.class);

    String _id;
    String _endpoint;
    PeerAccessor _peerAccessor;

    BaseService(String id)
    {
        this(id, null);
    }

    BaseService(String id, String endpoint)
    {
        this._id = id;
        if (endpoint == null) {
            endpoint = "default";
        }
        this._endpoint = endpoint;
        String serviceId = this._id + "-" + this._endpoint;
        this._peerAccessor = new PeerAccessor(ListHelper.Path(serviceId));
    }

    public PeerAccessor getPeerAccessor() {
        return this._peerAccessor;
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
