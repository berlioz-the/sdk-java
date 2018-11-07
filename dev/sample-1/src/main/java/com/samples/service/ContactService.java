package com.samples.service;

import com.samples.entity.Contact;
import com.samples.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository repository;

    public List<Contact> getAllContacts(){
        return repository.findAll();
    }
}
