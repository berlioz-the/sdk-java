package com.berlioz.http;

import com.berlioz.Executor;
import com.berlioz.PeerAccessor;
import com.berlioz.msg.BaseEndpoint;
import com.berlioz.msg.Endpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.Map;

public class RestTemplate extends org.springframework.web.client.RestTemplate {

    private static Logger logger = LogManager.getLogger(RestTemplate.class);

    private PeerAccessor _peerAccessor;

    public RestTemplate(PeerAccessor peerAccessor)
    {
        this._peerAccessor = peerAccessor;
    }

    @Override
    public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> urlVariables) throws RestClientException {
        return super.execute(massageUrl(url), method, requestCallback, responseExtractor, urlVariables);
    }

    @Override
    public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... urlVariables) throws RestClientException {
        return super.execute(massageUrl(url), method, requestCallback, responseExtractor, urlVariables);
    }

    @Override
    protected <T> T doExecute(final URI url,
                              final HttpMethod method,
                              final RequestCallback requestCallback,
                              final ResponseExtractor<T> responseExtractor) throws RestClientException {

        Executor<T, RestClientException> executor = new Executor<T, RestClientException>();
        executor.zipkin(_peerAccessor.getRemoteName(), method.toString());
        executor.selector(this._peerAccessor.getRandomSelector());
        executor.action(new Executor.IAction<T, RestClientException>() {
            public T perform(BaseEndpoint basePeer, brave.Span span) throws RestClientException {
                Endpoint peer = (Endpoint)basePeer;
//                String newUrlStr = String.format("%s://%s:%d%s", "http", "localhost", 40002, url.getRawPath());
                String newUrlStr = String.format("%s://%s:%d%s", peer.getProtocol(), peer.getAddress(), peer.getPort(), url.getRawPath());
                URI actualUrl = URI.create(newUrlStr);
                logger.debug("Request to: {}, Method: {}", actualUrl, method);
                return RestTemplate.super.doExecute(actualUrl, method, requestCallback, responseExtractor);
        }});
        return executor.run();
    }

    private String massageUrl(String url) {
        return "http://0.0.0.0/" + url;
    }
}
