package com.samples;

import com.berlioz.Berlioz;
import com.berlioz.Zipkin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {

    @RequestMapping("/greeting")
    public String index() {

        String result = Berlioz.service("app").request().getForObject("/", String.class);

        Zipkin.Span childSpan = Zipkin.getInstance().childSpan("db", "INSERT");
        try {
            Thread.sleep(50);
        } catch (Exception ex) {

        }
        childSpan.finish();

        return "Greetings!!! "; // + result;
        //Berlioz.service("app").all().toString() ;
    }

}