package com.bootcamp.desafioquality.service.validation;

import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.date.DateRangeValidator;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.function.Function;

public class ServiceValidator {
    protected final Function<String, RuntimeException> exceptionSupplier;

    public ServiceValidator(Function<String, RuntimeException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public void validateEmail(@Nullable String email) {
        if (!EmailValidator.isEmailValid(email)) {
            throw exceptionSupplier.apply(ServiceValidationError.INVALID_MAIL_FORMAT.getMessage());
        }
    }

    public void validateDates(String dateFromString, String dateToString) {
        Date dateFrom = DateParser.fromString(dateFromString);
        Date dateTo = DateParser.fromString(dateToString);
        final DateRangeValidator dateRangeValidator = new DateRangeValidator(exceptionSupplier);
        dateRangeValidator.validateDateTo(dateFrom);
        dateRangeValidator.validateDateTo(dateTo);
    }

    public void validatePeopleAmount(String peopleAmount) {
        try {
            int intAmount = Integer.parseInt(peopleAmount);
            if (intAmount <= 0) {
                throw exceptionSupplier.apply(ServiceValidationError.INVALID_PEOPLE_AMOUNT.getMessage());
            }
        } catch (Exception e) {
            throw exceptionSupplier.apply(ServiceValidationError.INVALID_PEOPLE_AMOUNT_TYPE.getMessage());
        }
    }

    public void validateLocation(String location) {
        if (location == null || !Location.exists(location)) {
            throw exceptionSupplier.apply(String.format(Location.LocationNotFoundException.MESSAGE, location));
        }
    }

    public void validatePaymentMethod(@Nullable String paymentMethodType, @Nullable Integer installments) {
        if (paymentMethodType == null)
            throw exceptionSupplier.apply(ServiceValidationError.EMPTY_PAYMENT_METHOD.getMessage());
        if (installments == null)
            throw exceptionSupplier.apply(ServiceValidationError.EMPTY_INSTALLMENTS.getMessage());
        PaymentMethodType pmType = PaymentMethodType.fromLabelOrElseThrow(paymentMethodType, () -> exceptionSupplier.apply(PaymentMethodType.PaymentMethodTypeError.PAYMENT_METHOD_TYPE_NOT_FOUND.getMsg(paymentMethodType)));
        pmType.getInterest(installments); // esto por dentro valida
    }
}
