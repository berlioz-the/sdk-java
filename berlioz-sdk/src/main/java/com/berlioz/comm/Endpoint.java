package com.berlioz.comm;

public class Endpoint extends BaseEndpoint {
    private String protocol;
    private String networkProtocol;
    private int port;
    private String address;

    public String getProtocol() {
        return protocol;
    }

    public String getNetworkProtocol() {
        return networkProtocol;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

}
