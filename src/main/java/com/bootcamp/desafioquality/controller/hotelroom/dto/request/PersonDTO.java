package com.bootcamp.desafioquality.controller.hotelroom.dto.request;

public class PersonDTO {
    private String dni;
    private String name;
    private String lastName;
    private String birthDate;
    private String mail;

    public String getDni() {
        return dni;
    }

    public PersonDTO setDni(String dni) {
        this.dni = dni;
        return this;
    }

    public String getName() {
        return name;
    }

    public PersonDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public PersonDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public PersonDTO setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getMail() {
        return mail;
    }

    public PersonDTO setMail(String mail) {
        this.mail = mail;
        return this;
    }
}
