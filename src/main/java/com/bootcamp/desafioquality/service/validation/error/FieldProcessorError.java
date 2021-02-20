package com.bootcamp.desafioquality.service.validation.error;

public enum FieldProcessorError {
    INVALID_PEOPLE_AMOUNT_TYPE("La cantidad de personas debe ser un numero entero"),
    INVALID_PEOPLE_AMOUNT("Cantidad de personas invalida: %s"),
    INVALID_MAIL_FORMAT("Por favor ingrese un e-mail valido"),
    EMPTY_PAYMENT_METHOD("Medio de pago vacio"),
    EMPTY_INSTALLMENTS("Cuotas vacias"),
    EMPTY_CARD_NUMBER("Numero de tarjeta vacio"),
    EMPTY_PAYMENT_METHOD_TYPE("Tipo de medio de pago vacio"),
    EMPTY_PEOPLE_AMOUNT("Cantidad de personas vacia"),
    PEOPLE_AMOUNT_AND_PEOPLE_LIST_SIZE_MISMATCH("La cantidad de personas indicada no coincide con el numero de personas en la lista"),
    EMPTY_PEOPLE_LIST("Lista de personas vacia"),
    EMPTY_FIELD("El campo '%s' es obligatorio"),
    INVALID_DNI_VALUE("Valor de dni %s invalido");

    public String getMessage(Object ... args) {
        return String.format(message, args);
    }

    private final String message;

    FieldProcessorError(String msg) {
        this.message = msg;
    }
}
