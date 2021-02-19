package com.bootcamp.desafioquality.service.hotelroom.exception;

public enum HotelRoomServiceError {
    INVALID_ROOM_TYPE("El tipo de habitación seleccionada no coincide con la cantidad de personas que se alojarán en ella"),
    INVALID_MAIL_FORMAT("Por favor ingrese un e-mail valido");

    public String getMessage(Object ... args) {
        return String.format(message, args);
    }

    private final String message;

    HotelRoomServiceError(String msg) {
        this.message = msg;
    }
}
