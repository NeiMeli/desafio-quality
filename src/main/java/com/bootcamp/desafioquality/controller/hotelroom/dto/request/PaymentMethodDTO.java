package com.bootcamp.desafioquality.controller.hotelroom.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.bootcamp.desafioquality.controller.dtoutil.Message.REQUIRED_FIELD;

public class PaymentMethodDTO {
    @Valid
    @NotEmpty(message = REQUIRED_FIELD) private String type;

    @Valid
    @NotEmpty(message = REQUIRED_FIELD) private String number;

    @Valid
    @NotNull(message = REQUIRED_FIELD) private Integer dues;

    public String getType() {
        return type;
    }

    public PaymentMethodDTO setType(String type) {
        this.type = type;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public PaymentMethodDTO setNumber(String number) {
        this.number = number;
        return this;
    }

    public Integer getDues() {
        return dues;
    }

    public PaymentMethodDTO setDues(Integer dues) {
        this.dues = dues;
        return this;
    }
}
