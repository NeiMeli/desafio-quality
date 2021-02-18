package com.bootcamp.desafioquality.controller.hotelroom.dto.request;

public class PaymentMethodDTO {
    private String type;
    private String number;
    private Integer dues;

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
