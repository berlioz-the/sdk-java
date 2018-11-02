package com.samples;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import zipkin2.Endpoint;
import zipkin2.Span;
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

    Reporter<Span> reporter = AsyncReporter.create(sender);
    //Reporter.CONSOLE; //

    @RequestMapping("/")
    public String index() {

        Endpoint localEndpoint = Endpoint.newBuilder().serviceName("mama").ip("192.168.0.1").build();

        long start = 1000 * System.currentTimeMillis();

        Span span = Span.newBuilder()
                .traceId("d3d200866a77cc63")
                .id("d3d200866a77cc63")
                .name("targz")
                .localEndpoint(localEndpoint)
                .timestamp(start)
                .duration(10000)
                .addAnnotation(start, "RECV")
                .addAnnotation(start + 1000, "SENT")
                .putTag("census.status_code", "OK")
                .build();


        reporter.report(span);

        return "Hello World!  " + start;
    }

}