package com.berlioz.comm;

import java.util.List;
import java.util.Map;

public class Message {
    private Map<String, Endpoint> endpoints;
    private Policy policies;
    private List<Consumed> consumes;
    private Peers peers;
}
