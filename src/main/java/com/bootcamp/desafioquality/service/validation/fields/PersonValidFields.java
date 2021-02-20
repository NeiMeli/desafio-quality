package com.bootcamp.desafioquality.service.validation.fields;

import java.util.function.Function;

public class PersonValidFields implements FieldValidityEnsurer {
    private String dni;
    private String name;
    private String lastName;
    private String birthDate;
    private String mail;
    protected final Function<String, RuntimeException> exceptionSupplier;

    public PersonValidFields(Function<String, RuntimeException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public String getDni() {
        ensureFieldWasValidated(dni, "Email");
        return dni;
    }

    public PersonValidFields setDni(String dni) {
        this.dni = dni;
        return this;
    }

    public String getName() {
        ensureFieldWasValidated(name, "name");
        return name;
    }

    public PersonValidFields setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastName() {
        ensureFieldWasValidated(lastName, "lastName");
        return lastName;
    }

    public PersonValidFields setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getBirthDate() {
        ensureFieldWasValidated(birthDate, "birthDate");
        return birthDate;
    }

    public PersonValidFields setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getMail() {
        ensureFieldWasValidated(mail, "mail");
        return mail;
    }

    public PersonValidFields setMail(String mail) {
        this.mail = mail;
        return this;
    }

    @Override
    public Function<String, RuntimeException> getExceptionSupplier() {
        return this.exceptionSupplier;
    }
}
