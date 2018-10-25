package com.berlioz;

import com.berlioz.comm.BaseEndpoint;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class PeerAccessor {

    private static Logger logger = LogManager.getLogger(Berlioz.class);
    private Registry _registry = Berlioz.registry;
    private List<String> _peerPath;
    private RandomSelector _randomSelector = new RandomSelector();
    private FirstSelector _firstSelector = new FirstSelector();

    PeerAccessor(List<String> peerPath)
    {
        this._peerPath = peerPath;
    }


    public void monitorAll(Registry.Callback<Map<String, BaseEndpoint>> cb)
    {
        this._registry.subscribe("peers", this._peerPath, cb);
    }

    public void monitorFirst(Registry.Callback<BaseEndpoint> cb)
    {
        monitorPeer(this._firstSelector, cb);
    }

    private void monitorPeer(final ISelector selector, final Registry.Callback<BaseEndpoint> cb)
    {
        this._registry.subscribe("peers", this._peerPath, new Registry.Callback<Map<String, BaseEndpoint>>() {
            BaseEndpoint oldValue = null;
            public void callback(Map<String, BaseEndpoint> map) {
                BaseEndpoint value = selector.select(map);
                if (EqualsBuilder.reflectionEquals(oldValue, value)) {
                    return;
                }
                oldValue = value;
                cb.callback(value);
            }
        });
    }

    public Map<String, BaseEndpoint> all()
    {
        Map<String, BaseEndpoint> peers = this._registry.get("peers", this._peerPath);
        if (peers == null) {
            return new HashMap<String, BaseEndpoint>();
        }
        return peers;
    }

    public BaseEndpoint first()
    {
        Map<String, BaseEndpoint> peers = this.all();
        return this._firstSelector.select(peers);
    }

    public BaseEndpoint random()
    {
        Map<String, BaseEndpoint> peers = this.all();
        return this._randomSelector.select(peers);
    }

    public interface ISelector {
        BaseEndpoint select(Map<String, BaseEndpoint> map);
    }

    public class RandomSelector implements ISelector {
        private Random _random = new Random();

        public BaseEndpoint select(Map<String, BaseEndpoint> map) {
            Set<String> mySet = map.keySet();
            if (mySet.size() == 0) {
                return null;
            }
            String[] keyArray = mySet.toArray(new String[mySet.size()]);
            return map.get(keyArray[_random.nextInt(keyArray.length)]);
        }
    }

    public class FirstSelector implements ISelector {
        public BaseEndpoint select(Map<String, BaseEndpoint> map) {
            Set<String> mySet = map.keySet();
            if (mySet.size() == 0) {
                return null;
            }
            String[] keyArray = mySet.toArray(new String[mySet.size()]);
            Arrays.sort(keyArray);
            return map.get(keyArray[0]);
        }
    }

}
