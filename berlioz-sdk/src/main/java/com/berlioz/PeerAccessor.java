package com.berlioz;

import com.berlioz.msg.BaseEndpoint;
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

    ISelector getRandomSelector() {
        return this._randomSelector;
    }

    ISelector getFirstSelector() {
        return this._randomSelector;
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
                BaseEndpoint value = selector.selectFrom(map);
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
        return this._firstSelector.select();
    }

    public BaseEndpoint random()
    {
        return this._randomSelector.select();
    }

    public interface ISelector {
        BaseEndpoint select();
        BaseEndpoint selectFrom(Map<String, BaseEndpoint> map);
    }

    public class RandomSelector implements ISelector {
        private Random _random = new Random();

        public BaseEndpoint select() {
            Map<String, BaseEndpoint> map = all();
            return this.selectFrom(map);
        }

        public BaseEndpoint selectFrom(Map<String, BaseEndpoint> map) {
            Set<String> mySet = map.keySet();
            if (mySet.size() == 0) {
                return null;
            }
            String[] keyArray = mySet.toArray(new String[mySet.size()]);
            return map.get(keyArray[_random.nextInt(keyArray.length)]);
        }
    }

    public class FirstSelector implements ISelector {
        public BaseEndpoint select() {
            Map<String, BaseEndpoint> map = all();
            return this.selectFrom(map);
        }

        public BaseEndpoint selectFrom(Map<String, BaseEndpoint> map) {
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
