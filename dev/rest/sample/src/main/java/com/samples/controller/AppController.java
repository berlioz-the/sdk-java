package com.samples.controller;

import com.berlioz.Berlioz;
import com.berlioz.Registry;
import com.berlioz.http.RestTemplate;
import com.berlioz.msg.Endpoint;
import com.samples.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AppController {

    public AppController() {
        Berlioz.service("app").monitorAll(new Registry.Callback<Map<String, Endpoint>>() {
            @Override
            public void callback(Map<String, Endpoint> value) {

            }
        });

        Berlioz.service("app").monitorFirst(new Registry.Callback<Endpoint>() {
            @Override
            public void callback(Endpoint value) {

            }
        });
    }

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