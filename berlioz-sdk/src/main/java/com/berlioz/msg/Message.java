package com.berlioz.msg;

import java.util.List;
import java.util.Map;

public class Message {
    public Map<String, Endpoint> endpoints;
    public Policy policies;
    public List<Consumed> consumes;
    public Peers peers;
}
