package com.berlioz;

import brave.Tracer;
import brave.Tracing;
import brave.internal.Nullable;
import brave.propagation.B3Propagation;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import com.berlioz.msg.Endpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import zipkin2.codec.Encoding;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

        // TODO: DEBUGGING!!
//        _sender = URLConnectionSender.newBuilder()
//                .endpoint("http://localhost:40009" + "/api/v2/spans")
//                .encoding(Encoding.JSON)
//                .build();
//        _reporter = AsyncReporter.create(_sender); // Reporter.CONSOLE; //
//        _tracing = Tracing.newBuilder()
//                .localServiceName(_localName)
//                .spanReporter(_reporter)
//                .build();
//        _tracer = _tracing.tracer();
//        _isEnabled = true;
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
        _reporter = Reporter.CONSOLE;// AsyncReporter.create(_sender);
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

    TraceContext.Extractor<HttpServletRequest> extractor =
        B3Propagation.B3_STRING.extractor(new Propagation.Getter<HttpServletRequest, String>() {
            @Nullable
            public String get(HttpServletRequest request, String header) {
                return request.getHeader(header);
            }
        });

    TraceContext.Injector<ClientHttpRequest> injector =
        B3Propagation.B3_STRING.injector(new Propagation.Setter<ClientHttpRequest, String>() {
            @Override
            public void put(ClientHttpRequest httpServletRequest, String header, String value) {
                logger.debug("Setting header {} = {}", header, value);
                httpServletRequest.getHeaders().set(header, value);
            }
        });

    public void startServer(HttpServletRequest request)
    {
        if (!_isEnabled) {
            return;
        }

        brave.Span span = null;

        TraceContextOrSamplingFlags contextOrFlags = extractor.extract(request);
        if (contextOrFlags == TraceContextOrSamplingFlags.EMPTY) {
            span = _tracer.newTrace()
                    .name(request.getMethod())
                    .tag("http.url", request.getRequestURL().toString())
                    .annotate("sr");
        } else if (contextOrFlags == TraceContextOrSamplingFlags.NOT_SAMPLED) {
            span = null;
        }
        else
        {
            span = _tracer.nextSpan(contextOrFlags);
        }

        if (span == null) {
            return;
        }
        logger.debug("Start server span: {}", span);
        request.setAttribute("BERLIOZ_SPAN", span);
    }

    public void finishServer(HttpServletRequest request, HttpServletResponse response)
    {
        brave.Span span = getSpanFromRequest(request);
        if (span == null) {
            return;
        }
        span.tag("http.status_code", String.valueOf(response.getStatus()));
        span.annotate("ss");
    }

    public Span childSpan(String name, String action)
    {
        brave.Span parentSpan = getCurrentSpan();
        if (parentSpan == null) {
            return NOOP_SPAN;
        }
        if (!_isEnabled) {
            return NOOP_SPAN;
        }

        brave.Span childSpan = _tracer.newChild(parentSpan.context()).remoteServiceName(name).name(action);
        return new RealSpan(childSpan);
    }

    private brave.Span getCurrentSpan() {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request == null) {
            return defaultSpan();
        }
        return getSpanFromRequest(request);
    }

    private brave.Span getSpanFromRequest(HttpServletRequest request) {
        Object obj = request.getAttribute("BERLIOZ_SPAN");
        if (obj instanceof  brave.Span) {
            return (brave.Span)obj;
        }
        return defaultSpan();
    }

    private brave.Span defaultSpan() {
        return null;
    }

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            return request;
        }
        return null;
    }

    public interface Span {
        boolean isSampled();
        Span annotate(String value);
        Span tag(String key, String value);
        void inject(ClientHttpRequest request);
        void finish();
    }

    class RealSpan implements Span
    {
        brave.Span _innerSpan;

        RealSpan(brave.Span innerSpan)
        {
            this._innerSpan = innerSpan;
            this._innerSpan.annotate("cs");
        }

        public void inject(ClientHttpRequest request)
        {
            injector.inject(_innerSpan.context(), request);
        }

        public boolean isSampled()
        {
            return true;
        }

        public Span annotate(String value)
        {
            this._innerSpan.annotate(value);
            return this;
        }

        public Span tag(String key, String value)
        {
            this._innerSpan.tag(key, value);
            return this;
        }

        public void finish()
        {
            this._innerSpan.annotate("cr");
        }

        @Override
        public String toString() {
            return this._innerSpan.toString();
        }
    }

    NoopSpan NOOP_SPAN = new NoopSpan();

    class NoopSpan implements Span
    {
        NoopSpan()
        {
        }

        public void inject(ClientHttpRequest request)
        {
        }

        public boolean isSampled()
        {
            return false;
        }

        public Span annotate(String value)
        {
            return this;
        }

        public Span tag(String key, String value)
        {
            return this;
        }

        public void finish()
        {
        }
    }

}
