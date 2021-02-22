package com.bootcamp.desafioquality.controller.hotelroom.dto.response;

import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.dtoutil.PersonDTOBuilder;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.service.hotelroom.impl.validfields.HotelRoomValidFields;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;

public class HotelRoomBookingResponseDTOBuilder {
    public static final String SUCCESS_MESSAGE = "El proceso termino satisfactoriamente";
    private final HotelRoomBookingRequestDTO request;
    private final HotelRoomValidFields validFields;
    private @Nullable String error;
    private double amount;
    private double interest;
    private String hotelCode;

    public HotelRoomBookingResponseDTOBuilder(HotelRoomBookingRequestDTO requestDTO, HotelRoomValidFields validFields) {
        this.request = requestDTO;
        this.validFields = validFields;
        error = null;
    }

    public HotelRoomBookingResponseDTOBuilder withError(@NotNull String error) {
        this.error = error;
        return this;
    }

    public HotelRoomBookingResponseDTO build() {
        HotelRoomBookingResponseDTO hotelRoomBookingResponseDTO = new HotelRoomBookingResponseDTO();
        buildBooking(hotelRoomBookingResponseDTO);
        hotelRoomBookingResponseDTO.setUserName(request.getUserName());
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO();
        if (error != null) {
            statusCodeDTO.setCode(HttpStatus.BAD_REQUEST.value());
            statusCodeDTO.setMessage(error);
        } else {
            statusCodeDTO.setCode(HttpStatus.OK.value());
            statusCodeDTO.setMessage(SUCCESS_MESSAGE);
            hotelRoomBookingResponseDTO.setAmount(amount);
            hotelRoomBookingResponseDTO.setInterest(interest);
            hotelRoomBookingResponseDTO.setTotal(amount * (1 + interest / 100));
        }
        hotelRoomBookingResponseDTO.setStatusCode(statusCodeDTO);

        return hotelRoomBookingResponseDTO;
    }

    private void buildBooking(HotelRoomBookingResponseDTO dto) {
        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();
        bookingResponseDTO.setDateFrom(DateParser.toString(validFields.getDateFrom()))
                .setDateTo(DateParser.toString(validFields.getDateTo()))
                .setHotelCode(this.hotelCode)
                .setDestination(validFields.getDestination().getLabel())
                .setRoomType(validFields.getRoomType().getLabel())
                .setPeopleAmount(String.valueOf(validFields.getPeopleAmount()))
                .setPeople(validFields.getPersonValidatedFields().stream().map(PersonDTOBuilder::fromValidFields).collect(Collectors.toList()));
    }

    public void withAmount(double amount) {
        this.amount = amount;
    }

    public void withInterest(double interest) {
        this.interest = interest;
    }

    public void withHotelCode(@NotNull String hotelCode) {
        this.hotelCode = hotelCode;
    }
}
