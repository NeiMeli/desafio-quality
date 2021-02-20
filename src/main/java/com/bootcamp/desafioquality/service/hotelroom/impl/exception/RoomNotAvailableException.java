package com.bootcamp.desafioquality.service.hotelroom.impl.exception;

public class RoomNotAvailableException extends Exception {
    public static final String MESSAGE = "La habitacion no esta disponible en el rango solicitado";
    public RoomNotAvailableException() {
        super(MESSAGE);
    }
}
