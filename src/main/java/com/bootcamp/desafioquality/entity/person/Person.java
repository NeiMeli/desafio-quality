package com.bootcamp.desafioquality.entity.person;

import com.bootcamp.desafioquality.entity.Persistable;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Person implements Persistable<Integer> {
    private int dni;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;

    public static Person fromJson(JsonNode jn) {
        Person person = new Person();
        person.setDni(jn.get("dni").intValue())
            .setFirstName(jn.get("nombre").textValue())
            .setLastName(jn.get("apellido").textValue())
            .setBirthDate(jn.get("fechaNacimiento").textValue())
            .setEmail(jn.get("e-mail").textValue());
        return person;
    }

    public int getDni() {
        return dni;
    }

    public Person setDni(int dni) {
        this.dni = dni;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Person setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Person setBirthDate(String birthDate) {
        try {
            this.birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(birthDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Person setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public Integer getPrimaryKey() {
        return this.dni;
    }

    @Override
    public boolean isNew() {
        return this.dni == 0;
    }

    @Override
    public void setId(@NotNull Integer id) {
        setDni(id);
    }
}
