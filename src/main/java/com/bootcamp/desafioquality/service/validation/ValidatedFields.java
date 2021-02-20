package com.bootcamp.desafioquality.service.validation;

import com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType;

import java.util.Date;
import java.util.function.Function;

public class ValidatedFields {
    public static final String UNCHECKED_FIELD_MSG = "El campo %s no fue validado";
    private String email;
    private Date dateFrom;
    private Date dateTo;
    protected Integer peopleAmount;
    private PaymentMethodType paymentMethodType;
    private Integer installments;
    private Double interest;

    public ValidatedFields(Function<String, RuntimeException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    protected final Function<String, RuntimeException> exceptionSupplier;

    protected void ensureFieldWasValidated(Object field, String name)  {
        if (field == null) {
            throw exceptionSupplier.apply(String.format(UNCHECKED_FIELD_MSG, name));
        }
    }

    public String getEmail() {
        ensureFieldWasValidated(email, "Email");
        return email;
    }

    public ValidatedFields setEmail(String email) {
        this.email = email;
        return this;
    }

    public Date getDateFrom() {
        ensureFieldWasValidated(dateFrom, "Fecha desde");
        return dateFrom;
    }

    public ValidatedFields setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public Date getDateTo() {
        ensureFieldWasValidated(dateTo, "Fecha hasta");
        return dateTo;
    }

    public ValidatedFields setDateTo(Date dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public Integer getPeopleAmount() {
        ensureFieldWasValidated(peopleAmount, "Cantidad de personas");
        return peopleAmount;
    }

    public ValidatedFields setPeopleAmount(Integer peopleAmount) {
        this.peopleAmount = peopleAmount;
        return this;
    }

    public PaymentMethodType getPaymentMethodType() {
        ensureFieldWasValidated(paymentMethodType, "Tipo de medio de pago");
        return paymentMethodType;
    }

    public ValidatedFields setPaymentMethodType(PaymentMethodType paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
        return this;
    }

    public int getInstallments() {
        return installments;
    }

    public ValidatedFields setInstallments(int installments) {
        ensureFieldWasValidated(installments, "Cuotas");
        this.installments = installments;
        return this;
    }

    public double getInterest() {
        ensureFieldWasValidated(interest, "Interes");
        return interest;
    }

    public ValidatedFields setInterest(double interest) {
        this.interest = interest;
        return this;
    }

    public boolean hasPeopleAmount() {
        return this.peopleAmount != null;
    }
}
