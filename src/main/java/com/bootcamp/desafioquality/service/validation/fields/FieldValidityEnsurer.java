package com.bootcamp.desafioquality.service.validation.fields;

import java.util.function.Function;

@FunctionalInterface
public interface FieldValidityEnsurer {
    String UNCHECKED_FIELD_MSG = "El campo %s no fue validado";

    Function<String, RuntimeException> getExceptionSupplier();

    default void ensureFieldWasValidated(Object field, String name) {
        if (field == null) {
            throw getExceptionSupplier().apply(String.format(UNCHECKED_FIELD_MSG, name));
        }
    }
}
