package com.samples;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {

    @RequestMapping("/greeting")
    public String index() {
        return "Greetings!!!";
    }

}