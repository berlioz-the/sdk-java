package com.samples.controller;

import com.berlioz.http.RestTemplate;
import com.samples.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/app")
    public String index() throws Exception {

        String output = "Greetings!!! <b>";

        Contact[] entries = restTemplate
                .getForObject("/entries", Contact[].class);

        for(Contact contact : entries)
        {
            output += contact.getName() + "<br />";
        }

        return output;
    }

}