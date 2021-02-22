package com.bootcamp.desafioquality.service.validation.processor;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PaymentMethodDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.date.DateRangeValidator;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType;
import com.bootcamp.desafioquality.service.validation.fields.CommonValidFields;
import com.bootcamp.desafioquality.service.validation.fields.PaymentMethodValidFields;
import com.bootcamp.desafioquality.service.validation.error.FieldProcessorError;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

public abstract class CommonValidFieldsProcessor {
    protected final Function<String, RuntimeException> exceptionSupplier;
    private final PersonValidFieldsProcessor personValidFieldsProcessor;

    public CommonValidFieldsProcessor(Function<String, RuntimeException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
        personValidFieldsProcessor = new PersonValidFieldsProcessor(exceptionSupplier);
    }

    public void validateEmail(@Nullable String email) {
        if (EmailValidator.isEmailInvalid(email)) {
            throw exceptionSupplier.apply(FieldProcessorError.INVALID_MAIL_FORMAT.getMessage());
        }
        getValidatedFields().setEmail(email);
    }

    protected abstract CommonValidFields getValidatedFields();

    public void validateDates(String dateFromString, String dateToString) {
        Date dateFrom = DateParser.fromStringOrElseThrow(dateFromString, exceptionSupplier);
        Date dateTo = DateParser.fromStringOrElseThrow(dateToString, exceptionSupplier);
        final DateRangeValidator dateRangeValidator = new DateRangeValidator(exceptionSupplier);
        dateRangeValidator.validateDateFrom(dateFrom);
        dateRangeValidator.validateDateTo(dateTo);
        getValidatedFields().setDateFrom(dateFrom);
        getValidatedFields().setDateTo(dateTo);
    }

    public void validatePeopleAmount(String peopleAmountParameter, int actualPeopleAmount) {
        if (Strings.isBlank(peopleAmountParameter)) {
            throw exceptionSupplier.apply(FieldProcessorError.EMPTY_PEOPLE_AMOUNT.getMessage());
        }
        int intAmount;
        try {
            intAmount = Integer.parseInt(peopleAmountParameter);

        } catch (Exception e) {
            throw exceptionSupplier.apply(FieldProcessorError.INVALID_PEOPLE_AMOUNT_TYPE.getMessage());
        }
        if (intAmount <= 0) {
            throw exceptionSupplier.apply(FieldProcessorError.INVALID_PEOPLE_AMOUNT.getMessage(intAmount));
        }
        if (intAmount != actualPeopleAmount) {
            throw exceptionSupplier.apply(FieldProcessorError.PEOPLE_AMOUNT_AND_PEOPLE_LIST_SIZE_MISMATCH.getMessage());
        }
        getValidatedFields().setPeopleAmount(intAmount);
    }

    public Location validateLocation(String location) {
        return Location.fromLabelOrElseThrow(location, exceptionSupplier);
    }

    public void validatePaymentMethod(@Nullable PaymentMethodDTO paymentMethodDTO) {
        if (paymentMethodDTO == null) {
            throw exceptionSupplier.apply(FieldProcessorError.EMPTY_PAYMENT_METHOD.getMessage());
        }
        String number = paymentMethodDTO.getNumber();
        if (Strings.isBlank(number)) {
            throw exceptionSupplier.apply(FieldProcessorError.EMPTY_CARD_NUMBER.getMessage());
        }
        String paymentMethodType = paymentMethodDTO.getType();
        if (Strings.isBlank(paymentMethodType)) {
            throw exceptionSupplier.apply(FieldProcessorError.EMPTY_PAYMENT_METHOD_TYPE.getMessage());
        }
        PaymentMethodType pmType = PaymentMethodType.fromLabelOrElseThrow(paymentMethodType, () -> exceptionSupplier.apply(PaymentMethodType.PaymentMethodTypeError.PAYMENT_METHOD_TYPE_NOT_FOUND.getMsg(paymentMethodType)));
        Integer installments = paymentMethodDTO.getDues();
        if (installments == null)
            throw exceptionSupplier.apply(FieldProcessorError.EMPTY_INSTALLMENTS.getMessage());
        double interest = pmType.getInterest(installments);
        PaymentMethodValidFields paymentMethodValidatedFields = getValidatedFields().getPaymentMethodValidatedFields();
        paymentMethodValidatedFields.setPaymentMethodType(pmType);
        paymentMethodValidatedFields.setInstallments(installments);
        paymentMethodValidatedFields.setInterest(interest);
    }

    protected void validateDestination(String destination) {
        Location location = validateLocation(destination);
        getValidatedFields().setDestination(location);
    }

    protected void validatePeopleList(List<PersonDTO> peopleList) {
        if (peopleList.isEmpty()) {
            throw exceptionSupplier.apply(FieldProcessorError.EMPTY_PEOPLE_LIST.getMessage());
        }
        peopleList.forEach(personDTO -> getValidatedFields().getPersonValidatedFields().add(personValidFieldsProcessor.validate(personDTO)));
    }
}
