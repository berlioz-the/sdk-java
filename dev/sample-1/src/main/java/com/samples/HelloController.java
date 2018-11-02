package com.samples;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import zipkin2.codec.Encoding;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

@RestController
public class HelloController {

    URLConnectionSender sender = URLConnectionSender.newBuilder()
            .endpoint("http://localhost:40009/api/v2/spans")
            .encoding(Encoding.JSON)
//            .connectTimeout(config.getConnectTimeout())
//            .compressionEnabled(config.isCompressionEnabled())
//            .readTimeout(config.getReadTimeout())
            .build();

    Reporter<zipkin2.Span> reporter = AsyncReporter.create(sender);
//    Reporter.CONSOLE; //

    @RequestMapping("/")
    public String index() {

        Tracing tracing = Tracing.newBuilder()
                .localServiceName("my-service")
                .spanReporter(reporter)
                .build();


        Tracer tracer = tracing.tracer();

        Span span = tracer.newTrace().remoteServiceName("some-remote").name("post").annotate("RECV");
        span.start();

        try {
            Thread.sleep(200);
        } catch (Exception ex) {

        }

        Span childSpan = tracer.newChild(span.context()).remoteServiceName("database").name("query").annotate("RECV").start();
        try {
            Thread.sleep(50);
        } catch (Exception ex) {

        }
        childSpan.finish();


        try {
            Thread.sleep(250);
        } catch (Exception ex) {

        }
        span.finish();

        tracing.close();

        return "Hello World!";
    }

}