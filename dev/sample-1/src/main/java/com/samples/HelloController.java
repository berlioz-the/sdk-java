package com.samples;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
//import zipkin.Span;
//import zipkin.reporter.AsyncReporter;
//import zipkin.reporter.Encoding;
//import zipkin.reporter.Reporter;
//import zipkin.reporter.Sender;
//import zipkin.reporter.okhttp3.OkHttpSender;
//import zipkin2.Endpoint;

import java.util.concurrent.TimeUnit;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
//        Reporter.CONSOLE.report(span);
//        AsyncReporter.create(URLConnectionSender.create)
//        Sender sender = OkHttpSender.builder()
//                .endpoint("http://localhost:40008" + "/api/v1/spans")
//                .encoding(Encoding.JSON)
//                .build();

//        AsyncReporter.

//        AsyncReporter.create(URLConnectionSender.create("http://localhost:9411/api/v2/spans"));

//        AsyncReporter<Span> reporter = AsyncReporter.builder(sender)
//                .closeTimeout(1, TimeUnit.MILLISECONDS)
//                .build();
//
//        Endpoint localEndpoint = Endpoint.newBuilder().serviceName("tweetie").ip("192.168.0.1").build();
//
//        span = Span.newBuilder()
//                .traceId("d3d200866a77cc59")
//                .id("d3d200866a77cc59")
//                .name("targz")
//                .localEndpoint(localEndpoint)
//                .timestamp(epochMicros())
//                .duration(durationInMicros)
//                .putTag("compression.level", "9");
//
//
//        return new Tuple<>(Tracing.newBuilder()
//                .localServiceName(serviceName)
//                .sampler(Sampler.ALWAYS_SAMPLE)
//                .traceId128Bit(true)
//                .reporter(reporter)
//                .build(), () -> reporter.flush());

        return "Hello World!";
    }

}