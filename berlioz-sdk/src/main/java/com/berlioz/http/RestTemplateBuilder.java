package com.berlioz.http;

import com.berlioz.Berlioz;
import com.berlioz.PeerAccessor;

public class RestTemplateBuilder {

    private String _service;
    private String _endpoint;

    public RestTemplateBuilder()
    {

    }

    public RestTemplateBuilder service(String value) {
        _service = value;
        return this;
    }

    public RestTemplateBuilder endpoint(String value) {
        _endpoint = value;
        return this;
    }

    public RestTemplate build()
    {
        PeerAccessor peerAccessor = Berlioz.service(_service, _endpoint).getPeerAccessor();
        RestTemplate restTemplate = new RestTemplate(peerAccessor);
        return restTemplate;
    }
}
