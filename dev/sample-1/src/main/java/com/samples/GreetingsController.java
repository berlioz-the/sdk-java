package com.samples;

import brave.Span;
import com.berlioz.Berlioz;
import com.berlioz.Zipkin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {

    @RequestMapping("/greeting")
    public String index() {

        String result = Berlioz.service("app").request().getForObject("/", String.class);

        Span childSpan = Zipkin.getInstance().childSpan("db", "INSERT").annotate("cs");
        try {
            Thread.sleep(50);
        } catch (Exception ex) {

        }
        childSpan.annotate("cr");

        return "Greetings!!! "; // + result;
        //Berlioz.service("app").all().toString() ;
    }

}