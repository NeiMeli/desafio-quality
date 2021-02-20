package com.bootcamp.desafioquality.service.validation.processor;

import com.bootcamp.desafioquality.service.validation.error.FieldProcessorError;

import java.lang.reflect.Field;
import java.util.function.Function;

public interface RequiredFieldsValidator <T> {
    default void validateRequiredFields(T dto) {
        Field[] fields = dto.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                Object value = f.get(dto);
                if (value == null) {
                    throw getExceptionSupplier().apply(FieldProcessorError.EMPTY_FIELD.getMessage(f.getName()));
                }
            } catch (IllegalAccessException e) {
                // ignore;
            }
        }
    }
    Function<String, RuntimeException> getExceptionSupplier();
}
