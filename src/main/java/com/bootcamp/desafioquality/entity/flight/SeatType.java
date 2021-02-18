package com.bootcamp.desafioquality.entity.flight;

import com.bootcamp.desafioquality.exception.BadRequestException;

import java.util.Arrays;

public enum SeatType {
    ECONOMY ("Economy"),
    BUSINESS ("Business");

    private final String label;

    SeatType(String label) {
        this.label = label;
    }

    public static SeatType fromLabel(String label) {
        return Arrays.stream(values())
                .filter(v -> v.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new SeatTypeNotFoundException(label));
    }

    public static class SeatTypeNotFoundException extends BadRequestException {
        public static final String MESSAGE = "Tipo de asiento %s inexistente";
        public SeatTypeNotFoundException(String value) {
            super(String.format(MESSAGE, value));
        }
    }
}
