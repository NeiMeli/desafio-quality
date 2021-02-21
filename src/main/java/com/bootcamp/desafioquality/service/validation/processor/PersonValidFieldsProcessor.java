package com.bootcamp.desafioquality.service.validation.processor;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.service.validation.error.FieldProcessorError;
import com.bootcamp.desafioquality.service.validation.fields.PersonValidFields;

import java.util.function.Function;

public class PersonValidFieldsProcessor implements RequiredFieldsValidator<PersonDTO> {
    private final Function<String, RuntimeException> exceptionSupplier;
    public PersonValidFieldsProcessor(Function<String, RuntimeException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public PersonValidFields validate(PersonDTO personDTO) {
        PersonValidFields personValidFields = new PersonValidFields(exceptionSupplier);
        validateRequiredFields(personDTO);
        if (EmailValidator.isEmailInvalid(personDTO.getMail())) {
            throw exceptionSupplier.apply(FieldProcessorError.INVALID_MAIL_FORMAT.getMessage());
        }
        String dni = personDTO.getDni();
        try {
            Integer.parseInt(dni);
        } catch (Exception e) {
            throw exceptionSupplier.apply(FieldProcessorError.INVALID_DNI_VALUE.getMessage(dni));
        }
        String birthDate = personDTO.getBirthDate();
        DateParser.fromStringOrElseThrow(birthDate, exceptionSupplier);

        personValidFields.setDni(dni);
        personValidFields.setMail(personDTO.getMail());
        personValidFields.setName(personDTO.getName());
        personValidFields.setLastName(personDTO.getLastName());
        personValidFields.setBirthDate(birthDate);
        return personValidFields;
    }

    @Override
    public Function<String, RuntimeException> getExceptionSupplier() {
        return exceptionSupplier;
    }
}
