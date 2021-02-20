package com.bootcamp.desafioquality.service.validation.fields;

import com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType;

import java.util.function.Function;

public class PaymentMethodValidFields implements FieldValidityEnsurer {
    private PaymentMethodType paymentMethodType;
    private Integer installments;
    private Double interest;
    protected final Function<String, RuntimeException> exceptionSupplier;

    public PaymentMethodValidFields(Function<String, RuntimeException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public PaymentMethodType getPaymentMethodType() {
        ensureFieldWasValidated(paymentMethodType, "Tipo de medio de pago");
        return paymentMethodType;
    }

    public PaymentMethodValidFields setPaymentMethodType(PaymentMethodType paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
        return this;
    }

    public int getInstallments() {
        return installments;
    }

    public PaymentMethodValidFields setInstallments(int installments) {
        ensureFieldWasValidated(installments, "Cuotas");
        this.installments = installments;
        return this;
    }

    public double getInterest() {
        ensureFieldWasValidated(interest, "Interes");
        return interest;
    }

    public PaymentMethodValidFields setInterest(double interest) {
        this.interest = interest;
        return this;
    }

    @Override
    public Function<String, RuntimeException> getExceptionSupplier() {
        return this.exceptionSupplier;
    }
}
