package com.berlioz.msg;

import java.util.Map;

public class Policy {
    private Map<String, Object> values;
    private Map<String, Policy> children;

    public Map<String, Object> getValues() {
        return values;
    }

    public Map<String, Policy> getChildren() {
        return children;
    }

}
