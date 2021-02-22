package com.bootcamp.desafioquality.common;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;

import java.util.function.Supplier;

public class PersonConstants {
    public static final Supplier<PersonDTO> VALID_PERSON_DTO_1 = () -> new PersonDTO()
            .setName("name1")
            .setLastName("lastName1")
            .setMail("mail1@gmail.com")
            .setDni("123456")
            .setBirthDate("10/05/1991");
    public static final Supplier<PersonDTO> VALID_PERSON_DTO_2 = () -> new PersonDTO()
            .setName("name2")
            .setLastName("lastName2")
            .setMail("mail2@gmail.com")
            .setDni("654321")
            .setBirthDate("14/11/1978");
}
