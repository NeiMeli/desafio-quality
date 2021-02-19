package com.bootcamp.desafioquality.service.validation;

public enum ServiceValidationError {
    INVALID_PEOPLE_AMOUNT_TYPE("La cantidad de personas debe ser un numero entero"),
    INVALID_PEOPLE_AMOUNT("Cantidad de personas invalida: %s"),
    INVALID_MAIL_FORMAT("Por favor ingrese un e-mail valido"),
    EMPTY_PAYMENT_METHOD("Medio de pago vacio"),
    EMPTY_INSTALLMENTS("Cuotas vacias");

    public String getMessage(Object ... args) {
        return String.format(message, args);
    }

    private final String message;

    ServiceValidationError(String msg) {
        this.message = msg;
    }
}
