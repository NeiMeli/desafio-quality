package com.bootcamp.desafioquality.controller.dtoutil;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.bootcamp.desafioquality.service.validation.fields.PersonValidFields;

public class PersonDTOBuilder {
    public static PersonDTO fromValidFields(PersonValidFields p) {
        return new PersonDTO()
                .setMail(p.getMail())
                .setDni(p.getDni())
                .setName(p.getName())
                .setLastName(p.getLastName())
                .setBirthDate(p.getBirthDate());
    }
}
