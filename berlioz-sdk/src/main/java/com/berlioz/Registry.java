package com.berlioz;

import com.google.common.base.Joiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registry {
    private static Logger logger = LogManager.getLogger(Registry.class);

    public interface Callback<T> {
        void callback(T value);
    }

    private Map<String, Map<String, Object>> _sections = new HashMap<String, Map<String, Object>>();
    private Map<String, List<Object>> _subscribers = new HashMap<String, List<Object>>();

    public Registry()
    {

    }

    public < T > void set(String sectionName, List<String> path, T value)
    {
        String pathStr = this._getPath(path);
        logger.info("SET {} :: {} => {}", sectionName, pathStr, value);
        Map<String, Object> section = this._getSection(sectionName);
        section.put(pathStr, value);

        this._notifyToSubscribers(sectionName, path);
    }

    public void reset(String sectionName, List<String> path)
    {
        logger.info("RESET {} :: {}", sectionName, this._getPath(path));
        this._sections.put(sectionName, new HashMap<String, Object>());
    }

    public < T > T get(String sectionName, List<String> path)
    {
        String pathStr = this._getPath(path);
//        logger.info("GET {} :: {} ...", sectionName, pathStr);
        Map<String, Object> section = this._getSection(sectionName);
        if (!section.containsKey(pathStr))
        {
            return (T)null;
        }
        Object value = section.get(pathStr);
        return (T)value;
    }

    public < T > void subscribe(String sectionName, List<String> path, Callback<T> cb)
    {
        logger.info("SUBSCRIBE {} :: {} ...", sectionName, this._getPath(path));
        String subscriberId = this._getSubscriberId(sectionName, path);
        if (!this._subscribers.containsKey(subscriberId)) {
            this._subscribers.put(subscriberId, new ArrayList<Object>());
        }
        this._subscribers.get(subscriberId).add(cb);

        this._notifyToSubscriber(sectionName, path, cb);
    }

    private <T> void _notifyToSubscribers(String sectionName, List<String> path) {
        String subscriberId = this._getSubscriberId(sectionName, path);
        if (!this._subscribers.containsKey(subscriberId)) {
            return;
        }
        T value = this.<T>get(sectionName, path);
        if (value == null) {
            return;
        }
        for(Object subscriber : this._subscribers.get(subscriberId)) {
            this.<T>_triggerToSubscriber(value, subscriber);
        }
    }

    private <T> void _notifyToSubscriber(String sectionName, List<String> path, Callback<T> cb) {
        T value = this.<T>get(sectionName, path);
        if (value == null) {
            return;
        }
        this.<T>_triggerToSubscriber(value, cb);
    }

    private <T> void _triggerToSubscriber(T value, Object subscriber) {
        Callback<T> myCallback = (Callback<T>)subscriber;
        myCallback.callback(value);
    }

    private String _getPath(List<String> path) {
        return Joiner.on("-").join(path);
    }

    private Map<String, Object> _getSection(String name)
    {
        if (!this._sections.containsKey(name)) {
            this._sections.put(name, new HashMap<String, Object>());
        }
        return this._sections.get(name);
    }

    private String _getSubscriberId(String sectionName, List<String> path) {
        return sectionName + "::" + this._getPath(path);
    }

}
