package com.samples.controller;

import com.samples.entity.Contact;
import com.samples.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @Autowired
    ContactRepository repository;

    @RequestMapping("/app")
    public String index() throws Exception {

        String output = "Greetings!!! <b>";

        for(Contact contact : repository.findAll())
        {
            output += contact.getName() + "<br />";
        }

        return output;
    }

}