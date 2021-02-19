package com.bootcamp.desafioquality.service.validation;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PaymentMethodDTO;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.date.DateRangeValidator;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType;
import org.apache.logging.log4j.util.Strings;
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
        Date dateFrom = DateParser.fromStringOrElseThrow(dateFromString, exceptionSupplier);
        Date dateTo = DateParser.fromStringOrElseThrow(dateToString, exceptionSupplier);
        final DateRangeValidator dateRangeValidator = new DateRangeValidator(exceptionSupplier);
        dateRangeValidator.validateDateFrom(dateFrom);
        dateRangeValidator.validateDateTo(dateTo);
    }

    public void validatePeopleAmount(String peopleAmountParameter, int actualPeopleAmount) {
        if (Strings.isBlank(peopleAmountParameter)) {
            throw exceptionSupplier.apply(ServiceValidationError.EMPTY_PEOPLE_AMOUNT.getMessage());
        }
        int intAmount;
        try {
            intAmount = Integer.parseInt(peopleAmountParameter);

        } catch (Exception e) {
            throw exceptionSupplier.apply(ServiceValidationError.INVALID_PEOPLE_AMOUNT_TYPE.getMessage());
        }
        if (intAmount <= 0) {
            throw exceptionSupplier.apply(ServiceValidationError.INVALID_PEOPLE_AMOUNT.getMessage(intAmount));
        }
        if (intAmount != actualPeopleAmount) {
            throw exceptionSupplier.apply(ServiceValidationError.PEOPLE_AMOUNT_AND_PEOPLE_LIST_SIZE_MISMATCH.getMessage());
        }

    }

    public void validateLocation(String location) {
        if (location == null || !Location.exists(location)) {
            throw exceptionSupplier.apply(String.format(Location.LocationNotFoundException.MESSAGE, location));
        }
    }

    public void validatePaymentMethod(@Nullable PaymentMethodDTO paymentMethodDTO) {
        if (paymentMethodDTO == null) {
            throw exceptionSupplier.apply(ServiceValidationError.EMPTY_PAYMENT_METHOD.getMessage());
        }
        String number = paymentMethodDTO.getNumber();
        if (Strings.isBlank(number)) {
            throw exceptionSupplier.apply(ServiceValidationError.EMPTY_CARD_NUMBER.getMessage());
        }
        String paymentMethodType = paymentMethodDTO.getType();
        if (Strings.isBlank(paymentMethodType)) {
            throw exceptionSupplier.apply(ServiceValidationError.EMPTY_PAYMENT_METHOD_TYPE.getMessage());
        }
        PaymentMethodType pmType = PaymentMethodType.fromLabelOrElseThrow(paymentMethodType, () -> exceptionSupplier.apply(PaymentMethodType.PaymentMethodTypeError.PAYMENT_METHOD_TYPE_NOT_FOUND.getMsg(paymentMethodType)));
        Integer installments = paymentMethodDTO.getDues();
        if (installments == null)
            throw exceptionSupplier.apply(ServiceValidationError.EMPTY_INSTALLMENTS.getMessage());
        pmType.getInterest(installments); // esto por dentro valida
    }
}
