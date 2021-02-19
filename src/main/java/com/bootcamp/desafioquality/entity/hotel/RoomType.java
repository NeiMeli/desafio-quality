package com.bootcamp.desafioquality.entity.hotel;

import com.bootcamp.desafioquality.exception.BadRequestException;

import java.util.Arrays;
import java.util.function.Supplier;

public enum RoomType {
    SINGLE ("Single", 1),
    DOBLE ("Doble", 2),
    TRIPLE ("Triple", 3),
    MULTIPLE("Multiple", Integer.MAX_VALUE);

    private final String label;
    private final int capacity;

    RoomType(String label, int capacity) {
        this.label = label;
        this.capacity = capacity;
    }

    public boolean hasCapacity(int capacity) {
        return this.capacity >= capacity;
    }

    public static RoomType fromLabel(String label) {
        return fromLabelOrElseThrow(label, () -> new RoomTypeNotFoundException(label));
    }

    public static RoomType fromLabelOrElseThrow(String label, Supplier<? extends RuntimeException> exSupplier) {
        return Arrays.stream(values())
                .filter(v -> v.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(exSupplier);
    }

    public static class RoomTypeNotFoundException extends BadRequestException {
        public static final String MESSAGE = "Tipo de habitacion %s inexistente";
        public RoomTypeNotFoundException(String value) {
            super(String.format(MESSAGE, value));
        }
    }
}


