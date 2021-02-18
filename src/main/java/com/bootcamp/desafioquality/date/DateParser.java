package com.bootcamp.desafioquality.date;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    private static final String FORMAT = "dd/MM/yyyy";

    public static Date fromString(@NotNull String dateString) {
        try {
            return new SimpleDateFormat(FORMAT).parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(String.format("'%s' no es una fecha valida. %s", dateString, e.getLocalizedMessage()));
        }
    }
}
