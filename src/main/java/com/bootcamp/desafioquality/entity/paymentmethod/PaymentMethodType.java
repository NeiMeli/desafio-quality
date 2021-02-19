package com.bootcamp.desafioquality.entity.paymentmethod;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public enum PaymentMethodType {
    CREDIT ("CREDIT", i -> {
        Optional<Double> interest = resolveInterest(i);
        if (interest.isEmpty()) {
            throw new PaymentMethodTypeException(PaymentMethodTypeError.INVALID_INSTALLMENT_AMOUNT.getMsg(i));
        }
        return interest.get();
    }),
    DEBIT ("DEBIT", i -> {
        if (i != 1) throw new PaymentMethodTypeException(PaymentMethodTypeError.INSTALLMENTS_NOT_ALLOWED.getMsg());
        return 0d;
    });

    private static final Map<Integer, Double> interestByInstallments = new HashMap<>();
    private final String label;

    PaymentMethodType(String label, Function<Integer, Double> interestResolver) {
        this.label = label;
        this.interestResolver = interestResolver;
    }

    public static PaymentMethodType fromLabel(String label) {
        return fromLabelOrElseThrow(label, () -> new PaymentMethodTypeException(PaymentMethodTypeError.PAYMENT_METHOD_TYPE_NOT_FOUND.getMsg(label)));
    }

    public static PaymentMethodType fromLabelOrElseThrow(String label, Supplier<? extends RuntimeException> exSupplier) {
        return Arrays.stream(values())
                .filter(v -> v.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(exSupplier);
    }

    private final Function<@NotNull Integer, Double> interestResolver;

    public double getInterest(int installments) {
        return interestResolver.apply(installments);
    }

    static {
        interestByInstallments.put(3, 5d);
        interestByInstallments.put(6, 10d);
        interestByInstallments.put(9, 20d);
        interestByInstallments.put(12, 25d);
        interestByInstallments.put(18, 40d);
    }

    private static Optional<Double> resolveInterest(int installments) {
        return Optional.ofNullable(interestByInstallments.get(installments));
    }

    public String getLabel() {
        return label;
    }

    public static class PaymentMethodTypeException extends RuntimeException {
        public PaymentMethodTypeException(String message) {
            super(message);
        }
    }

    public enum PaymentMethodTypeError {
        PAYMENT_METHOD_TYPE_NOT_FOUND("No existe el medio de pago %s"),
        INVALID_INSTALLMENT_AMOUNT("Valor de cuotas invalido: %s"),
        INSTALLMENTS_NOT_ALLOWED("No se permiten cuotas para este medio de pago");

        public String getMsg(Object ... args) {
            return String.format(msg, args);
        }

        private final String msg;

        PaymentMethodTypeError(String msg) {
            this.msg = msg;
        }

    }
}
