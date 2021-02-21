package com.bootcamp.desafioquality.service.flight.exception;

import com.bootcamp.desafioquality.exception.BadRequestException;

public class FlightServiceException extends BadRequestException {
    public FlightServiceException(String message) {
        super(message);
    }
}
