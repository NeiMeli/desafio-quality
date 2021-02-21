package com.bootcamp.desafioquality.entity.location;

import com.bootcamp.desafioquality.exception.BadRequestException;

import java.util.Arrays;
import java.util.function.Function;

public enum Location {
    BS_AS("Buenos Aires"),
    PUERTO_IGUAZU("Puerto Iguazu"),
    BOGOTA("Bogotá"),
    TUCUMAN("Tucumán"),
    MEDELLIN("Medellín"),
    BOCAGRANDE("Bocagrande"),
    CARTAGENA("Cartagena");

    public static boolean exists(String label) {
        return Arrays.stream(values())
                .anyMatch(v -> v.label.equalsIgnoreCase(label));
    }

    public String getLabel() {
        return label;
    }

    private final String label;

    Location(String label) {
        this.label = label;
    }

    public static Location fromLabel(String label) {
        return fromLabelOrElseThrow(label, LocationNotFoundException::new);
    }

    public static Location fromLabelOrElseThrow(String label, Function<String, RuntimeException> exSupplier) {
        return Arrays.stream(values())
                .filter(v -> v.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> exSupplier.apply(String.format(LocationNotFoundException.MESSAGE, label)));
    }

    public static class LocationNotFoundException extends BadRequestException {
        public static final String MESSAGE = "El destino %s no existe";
        public LocationNotFoundException(String value) {
            super(value);
        }
    }
}
