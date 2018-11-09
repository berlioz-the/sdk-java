package com.samples.entity;


import javax.persistence.*;

@Entity
@Table(name = "addressBook")
public class Contact {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
