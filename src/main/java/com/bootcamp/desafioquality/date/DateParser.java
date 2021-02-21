package com.bootcamp.desafioquality.date;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class DateParser {
    private static final String FORMAT = "dd/MM/yyyy";
    public static final String ERROR_MESSAGE = "'%s' no es una fecha valida.";

    public static Date fromString(@Nullable String dateString) {
        return fromStringOrElseThrow(dateString, RuntimeException::new);
    }

    public static Date fromStringOrElseThrow(@Nullable String dateString, Function<String, ? extends RuntimeException> exSupplier) {
        try {
            return new SimpleDateFormat(FORMAT).parse(dateString);
        } catch (Exception e) {
            throw exSupplier.apply(String.format(ERROR_MESSAGE, dateString));
        }
    }

    public static int getDaysBetween(Date dateFrom, Date dateTo) {
        long diffInMillies = Math.abs(dateFrom.getTime() - dateTo.getTime());
        return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static String toString(@NotNull Date date) {
        return new SimpleDateFormat(FORMAT).format(date);
    }
}
