package com.berlioz;

import com.berlioz.msg.Policy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyResolver {
    private static Logger logger = LogManager.getLogger(PolicyResolver.class);
    private Registry _registry;
    private Map<String, Object> _defaults = new HashMap<String, Object>();

    PolicyResolver(Registry registry) {
        _registry = registry;

        _defaults.put("enable-zipkin", true);
        _defaults.put("zipkin-endpoint", "");
        _defaults.put("timeout", 5000);
        _defaults.put("no-peer-retry", true);
        _defaults.put("retry-count", 3);
        _defaults.put("retry-initial-delay", 500);
        _defaults.put("retry-delay-multiplier", 2);
        _defaults.put("retry-max-delay", 5000);
    }

    public boolean resolveBool(String name, List<String> target) {
        Object val = this.resolve(name, target);
        if (val == null) {
            return false;
        }
        return ((Boolean) val).booleanValue();
    }

    public String resolveString(String name, List<String> target) {
        Object val = this.resolve(name, target);
        if (val == null) {
            return "";
        }
        return (String) val;
    }

    public int resolveInt(String name, List<String> target) {
        Object val = this.resolve(name, target);
        if (val == null) {
            return 0;
        }
        return ((Integer) val).intValue();
    }

    public Object resolve(String name, List<String> target) {
        Policy policy = _registry.<Policy>get("policies", ListHelper.Path());
        Object value = null;
        if (policy != null) {
            value = this._resolve(policy, name, target, 0);
        }
        if (value == null) {
            if (_defaults.containsKey(name)) {
                value = _defaults.get(name);
            }
        }
        return value;
    }

    private Object _resolve(Policy policy, String name, List<String> target, int index) {
        Object value = null;
        if (target.size() > index) {
            if (policy.getChildren() != null) {
                String targetName = target.get(index);
                if (policy.getChildren().containsKey(targetName)) {
                    Policy childPolicy = policy.getChildren().get(targetName);
                    value = this._resolve(childPolicy, name, target, index + 1);
                }
            }
        }
        if (value != null) {
            return value;
        }
        if (policy.getValues() != null) {
            if (policy.getValues().containsKey(name)) {
                return policy.getValues().get(name);
            }
        }
        return null;
    }

}