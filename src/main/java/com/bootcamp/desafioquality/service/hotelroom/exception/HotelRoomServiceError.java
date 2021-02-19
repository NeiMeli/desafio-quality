package com.bootcamp.desafioquality.service.hotelroom.exception;

public enum HotelRoomServiceError {
    EMPTY_ROOM_TYPE("No indica tipo de habitacion"),
    EMPTY_BOOKING("Booking vacio"),
    INVALID_ROOM_TYPE("El tipo de habitación seleccionada no coincide con la cantidad de personas que se alojarán en ella");

    public String getMessage(Object ... args) {
        return String.format(message, args);
    }

    private final String message;

    HotelRoomServiceError(String msg) {
        this.message = msg;
    }
}
