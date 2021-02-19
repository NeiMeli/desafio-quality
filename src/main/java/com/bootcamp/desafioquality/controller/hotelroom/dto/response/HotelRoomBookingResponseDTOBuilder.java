package com.bootcamp.desafioquality.controller.hotelroom.dto.response;

import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PaymentMethodDTO;
import com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class HotelRoomBookingResponseDTOBuilder {
    public static final String SUCCESS_MESSAGE = "El proceso termino satisfactoriamente";
    private final HotelRoomBookingRequestDTO request;
    private @Nullable String error;
    private double amount;

    public HotelRoomBookingResponseDTOBuilder(HotelRoomBookingRequestDTO requestDTO) {
        this.request = requestDTO;
        error = null;
    }

    public HotelRoomBookingResponseDTOBuilder withError(@NotNull String error) {
        this.error = error;
        return this;
    }

    public HotelRoomBookingResponseDTO build() {
        HotelRoomBookingResponseDTO hotelRoomBookingResponseDTO = new HotelRoomBookingResponseDTO();
        hotelRoomBookingResponseDTO.setBooking(request.getBooking());
        hotelRoomBookingResponseDTO.setUserName(request.getUserName());
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO();
        if (error != null) {
            statusCodeDTO.setCode(HttpStatus.BAD_REQUEST.value());
            statusCodeDTO.setMessage(error);
        } else {
            statusCodeDTO.setCode(HttpStatus.OK.value());
            statusCodeDTO.setMessage(SUCCESS_MESSAGE);
            hotelRoomBookingResponseDTO.setAmount(amount);
            PaymentMethodDTO paymentMethod = request.getBooking().getPaymentMethod();
            PaymentMethodType paymentMethodType = PaymentMethodType.fromLabel(paymentMethod.getType());
            double interest = paymentMethodType.getInterest(paymentMethod.getDues());
            hotelRoomBookingResponseDTO.setInterest(interest);
            hotelRoomBookingResponseDTO.setTotal(amount * (1 + interest / 100));
        }
        hotelRoomBookingResponseDTO.setStatusCode(statusCodeDTO);

        return hotelRoomBookingResponseDTO;
    }

    public void withAmount(double amount) {
        this.amount = amount;
    }
}