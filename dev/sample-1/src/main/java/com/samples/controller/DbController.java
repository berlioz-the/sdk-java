package com.samples.controller;

import com.samples.entity.Contact;
import com.samples.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbController {

    @Autowired
    private ContactService contactService;

    @RequestMapping("/")
    public String index() throws Exception {

        String output = "Greetings!!! <b>";

        for(Contact contact : contactService.getAllContacts())
        {
            output += contact.getName() + "<br />";
        }

        return output;
    }

}