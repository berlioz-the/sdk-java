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

        return "Greetings!!! <b>" + result + "</b>";
    }

}