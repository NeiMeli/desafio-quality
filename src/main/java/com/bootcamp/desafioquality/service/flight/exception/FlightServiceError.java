package com.bootcamp.desafioquality.service.flight.exception;

public enum FlightServiceError {
    FLIGHT_NOT_FOUND("Vuelo no encontrado");

    public String getMessage(Object ... args) {
        return String.format(message, args);
    }

    private final String message;

    FlightServiceError(String msg) {
        this.message = msg;
    }
}
