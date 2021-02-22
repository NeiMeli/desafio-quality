package com.bootcamp.desafioquality.common;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PaymentMethodDTO;
import com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType;

import java.util.function.Supplier;

public class PaymentMethodConstants {
    public static final Supplier<PaymentMethodDTO> VALID_PAYMENT_METHOD_DTO_1 = () -> {
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setType(PaymentMethodType.CREDIT.getLabel());
        paymentMethodDTO.setNumber("1234-1234");
        paymentMethodDTO.setDues(9);
        return paymentMethodDTO;
    };
    public static final Supplier<PaymentMethodDTO> VALID_PAYMENT_METHOD_DTO_2 = () -> {
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setType(PaymentMethodType.CREDIT.getLabel());
        paymentMethodDTO.setNumber("1234-1234");
        paymentMethodDTO.setDues(3);
        return paymentMethodDTO;
    };
}
