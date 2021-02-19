package com.bootcamp.desafioquality.date;

import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.bootcamp.desafioquality.date.DateRangeValidator.DateRangeError.INVALID_DATE_FROM;
import static com.bootcamp.desafioquality.date.DateRangeValidator.DateRangeError.INVALID_DATE_TO;

public class DateRangeValidator {
    public static final Predicate<@Nullable Date> DEFAULT_VALIDATION = date -> true;
    private final Function<String, RuntimeException> exceptionSupplier;
    private Predicate<@Nullable Date> dateFromValidation = DEFAULT_VALIDATION;
    private Predicate<@Nullable Date> dateToValidation = DEFAULT_VALIDATION;
    private Date dateFrom;
    private Date dateTo;


    public DateRangeValidator(Function<String, RuntimeException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public DateRangeValidator validateDateFrom(Date dateFrom) {
        if (!dateFromValidation.test(dateFrom)) {
            throw exceptionSupplier.apply(INVALID_DATE_FROM.getMsg());
        }
        this.dateFrom = dateFrom;
        dateToValidation = dateTo -> this.dateFrom.compareTo(dateTo) < 0;
        return this;
    }

    public DateRangeValidator validateDateTo(Date dateTo) {
        if (!dateToValidation.test(dateTo)) {
            throw exceptionSupplier.apply(INVALID_DATE_TO.getMsg());
        }
        this.dateTo = dateTo;
        dateFromValidation = dateFrom -> this.dateTo.compareTo(dateFrom) > 0;
        return this;
    }

    public void withoutDateFrom() {
        this.dateFrom = null;
        dateToValidation = DEFAULT_VALIDATION;
    }

    public void withoutDateTo() {
        this.dateTo = null;
        dateFromValidation = DEFAULT_VALIDATION;
    }


    public enum DateRangeError {
        INVALID_DATE_TO("La fecha de salida debe ser mayor a la de entrada"),
        INVALID_DATE_FROM("La fecha de entrada debe ser menor a la de salida");

        public String getMsg() {
            return msg;
        }

        private final String msg;

        DateRangeError(String msg) {
            this.msg = msg;
        }
    }
}
