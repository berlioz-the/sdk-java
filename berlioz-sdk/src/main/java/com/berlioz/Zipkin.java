package com.berlioz;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import com.berlioz.msg.Endpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import zipkin2.codec.Encoding;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Zipkin {
    private static Logger logger = LogManager.getLogger(Berlioz.class);

    private String _localName;
    private boolean _isEnabled = false;

    private URLConnectionSender _sender;
    private Reporter<zipkin2.Span> _reporter;
    private Tracing _tracing;
    private Tracer _tracer;

    public static Zipkin getInstance() {
        return Berlioz.zipkin;
    }

    Zipkin(PolicyResolver policy) {
        _localName = "service://" + System.getenv("BERLIOZ_CLUSTER")
            + "-" + System.getenv("BERLIOZ_SECTOR")
            + "-" + System.getenv("BERLIOZ_SERVICE");

        policy.monitorString("zipkin-service-id", ListHelper.Path(), new Registry.Callback<String>() {
            public void callback(String value) {
                _monitorServiceAddresses(value);
            }
        });
    }

    private void _monitorServiceAddresses(String serviceId) {
        logger.debug("DTrace Provider: {}", serviceId);
        this._cleanup();

        Service zipkinSvc = new Service(serviceId, "client");
        zipkinSvc.monitorFirst(new Registry.Callback<Endpoint>() {
            public void callback(Endpoint peer) {
                _setupServicePeer(peer);
            }
        });
    }

    private void _setupServicePeer(Endpoint peer) {
        _cleanup();

        _sender = URLConnectionSender.newBuilder()
                .endpoint(peer.getProtocol() + "://" + peer.getAddress() + ":" + peer.getPort() + "/api/v2/spans")
                .encoding(Encoding.JSON)
                .build();

        _reporter = AsyncReporter.create(_sender); // Reporter.CONSOLE;

        _tracing = Tracing.newBuilder()
                .localServiceName(_localName)
                .spanReporter(_reporter)
                .build();
        _tracer = _tracing.tracer();

        _isEnabled = true;
    }

    private void _cleanup() {
        _isEnabled = false;

        if (_tracer != null) {
            _tracer = null;
        }
        if (_tracing != null) {
            _tracing.close();
            _tracing = null;
        }
        if (_reporter != null) {
            _reporter = null;
        }
        if (_sender != null) {
            _sender.close();
            _sender = null;
        }
    }

    public void startServer(HttpServletRequest request)
    {
        brave.Span span = _tracer.newTrace()
                .name(request.getMethod())
                .tag("http.url", request.getRequestURL().toString())
                .annotate("sr");

        HttpSession session = request.getSession();
        session.setAttribute("BERLIOZ_SPAN", span);
    }

    public void finishServer(HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession();
        brave.Span span = (brave.Span)session.getAttribute("BERLIOZ_SPAN");
        span.tag("http.status_code", String.valueOf(response.getStatus()));
        span.annotate("ss");
    }

    public Span childSpan(String name, String action)
    {
        Span parentSpan = getCurrentSpan();
        if (parentSpan == null) {
            return null;
        }
        if (!_isEnabled) {
            return null;
        }
        return _tracer.newChild(parentSpan.context()).remoteServiceName(name).name(action);
    }

    private static Span getCurrentSpan() {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request == null) {
            return null;
        }
        HttpSession session = request.getSession();
        brave.Span span = (brave.Span)session.getAttribute("BERLIOZ_SPAN");
        return span;
    }

    private static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            return request;
        }
        return null;
    }

}
