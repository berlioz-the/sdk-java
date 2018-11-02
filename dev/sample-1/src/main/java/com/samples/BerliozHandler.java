package com.samples;

import brave.Tracer;
import brave.Tracing;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BerliozHandler extends HandlerInterceptorAdapter {


    URLConnectionSender sender = URLConnectionSender.newBuilder()
            .endpoint("http://localhost:40009/api/v2/spans")
            .encoding(Encoding.JSON)
            .build();

    Reporter<Span> reporter = AsyncReporter.create(sender);
//    Reporter.CONSOLE; //

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.printf("BERLIOZ:: preHandle\n");

        Tracing tracing = Tracing.newBuilder()
                .localServiceName("another-service")
                .spanReporter(reporter)
                .build();
        Tracer tracer = tracing.tracer();
        brave.Span span = tracer.newTrace()
                .name(request.getMethod())
                .tag("http.url", request.getRequestURL().toString())
                .annotate("sr");

        HttpSession session = request.getSession();
        session.setAttribute("BERLIOZ_SPAN", span);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        System.out.printf("BERLIOZ:: postHandle\n");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        System.out.printf("BERLIOZ:: afterCompletion\n");

        HttpSession session = request.getSession();
        brave.Span span = (brave.Span)session.getAttribute("BERLIOZ_SPAN");
        span.tag("http.status_code", String.valueOf(response.getStatus()));
        span.annotate("ss");
    }

}
