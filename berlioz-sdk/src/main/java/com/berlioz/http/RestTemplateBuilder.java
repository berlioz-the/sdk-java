package com.berlioz.http;

import com.berlioz.Berlioz;
import com.berlioz.PeerAccessor;

public class RestTemplateBuilder {

    private String _cluster;
    private String _sector;
    private String _service;
    private String _endpoint;

    public RestTemplateBuilder()
    {

    }

    public RestTemplateBuilder cluster(String value) {
        _cluster = value;
        return this;
    }

    public RestTemplateBuilder sector(String value) {
        _sector = value;
        return this;
    }

    public RestTemplateBuilder service(String value) {
        _service = value;
        return this;
    }

    public RestTemplateBuilder endpoint(String value) {
        _endpoint = value;
        return this;
    }

    public RestTemplate build() throws IllegalArgumentException
    {
        PeerAccessor peerAccessor;
        if (_cluster != null) {
            peerAccessor = Berlioz.cluster(_cluster, _endpoint).getPeerAccessor();
        } else if (_sector != null) {
            if (_service == null) {
                throw new IllegalArgumentException("service");
            }
            peerAccessor = Berlioz.sector(_sector).service(_service, _endpoint).getPeerAccessor();
        } else {
            if (_service == null) {
                throw new IllegalArgumentException("service");
            }
            peerAccessor = Berlioz.service(_service, _endpoint).getPeerAccessor();
        }
        RestTemplate restTemplate = new RestTemplate(peerAccessor);
        return restTemplate;
    }
}
