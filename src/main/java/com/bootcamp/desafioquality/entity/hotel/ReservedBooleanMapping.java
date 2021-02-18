package com.bootcamp.desafioquality.entity.hotel;

import com.bootcamp.desafioquality.exception.BadRequestException;

import java.util.Arrays;

public enum ReservedBooleanMapping {
    TRUE ("Si", true),
    FALSE ("No", false);

    private final String label;

    public String getLabel() {
        return label;
    }

    public boolean getValue() {
        return value;
    }

    private final boolean value;

    ReservedBooleanMapping(String label, boolean value) {
        this.label = label;
        this.value = value;
    }

    public static boolean fromLabel(String label) {
        return Arrays.stream(values())
                .filter(v -> v.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new ReservedBooleanMappingNotFoundException(label))
                .getValue();
    }

    public static class ReservedBooleanMappingNotFoundException extends BadRequestException {
        public static final String MESSAGE = "Mapeo de reservado %s inexistente";
        public ReservedBooleanMappingNotFoundException(String value) {
            super(String.format(MESSAGE, value));
        }
    }
}
