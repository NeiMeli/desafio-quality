package com.bootcamp.desafioquality.service.validation.fields;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class CommonValidFields implements FieldValidityEnsurer {
    private String email;
    private Date dateFrom;
    private Date dateTo;
    protected Integer peopleAmount;
    private final List<PersonValidFields> personValidatedFields;
    private final PaymentMethodValidFields paymentMethodValidatedFields;
    protected final Function<String, RuntimeException> exceptionSupplier;

    public CommonValidFields(Function<String, RuntimeException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
        this.personValidatedFields = new ArrayList<>();
        this.paymentMethodValidatedFields = new PaymentMethodValidFields(exceptionSupplier);
    }

    public String getEmail() {
        ensureFieldWasValidated(email, "Email");
        return email;
    }

    public CommonValidFields setEmail(String email) {
        this.email = email;
        return this;
    }

    public Date getDateFrom() {
        ensureFieldWasValidated(dateFrom, "Fecha desde");
        return dateFrom;
    }

    public CommonValidFields setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public Date getDateTo() {
        ensureFieldWasValidated(dateTo, "Fecha hasta");
        return dateTo;
    }

    public CommonValidFields setDateTo(Date dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public Integer getPeopleAmount() {
        ensureFieldWasValidated(peopleAmount, "Cantidad de personas");
        return peopleAmount;
    }

    public CommonValidFields setPeopleAmount(Integer peopleAmount) {
        this.peopleAmount = peopleAmount;
        return this;
    }

    public boolean hasPeopleAmount() {
        return this.peopleAmount != null;
    }

    public List<PersonValidFields> getPersonValidatedFields() {
        return personValidatedFields;
    }

    public PaymentMethodValidFields getPaymentMethodValidatedFields() {
        return paymentMethodValidatedFields;
    }

    @Override
    public Function<String, RuntimeException> getExceptionSupplier() {
        return exceptionSupplier;
    }
}
