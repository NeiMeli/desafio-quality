package com.bootcamp.desafioquality.service.flight.query;

import com.bootcamp.desafioquality.exception.BadRequestException;

public class FlightQueryException extends BadRequestException {
    public FlightQueryException(String message) {
        super(message);
    }
}
