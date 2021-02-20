package com.bootcamp.desafioquality.service.validation.processor;

import org.jetbrains.annotations.Nullable;

public class EmailValidator {
    private static final String regex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    public static boolean isEmailValid(@Nullable final String email) {
        return email != null && email.matches(regex);
    }
}
