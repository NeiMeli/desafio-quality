package com.bootcamp.desafioquality.controller.flight.dto.response;

import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.dtoutil.PersonDTOBuilder;
import com.bootcamp.desafioquality.controller.flight.dto.FlightReservationDTO;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.service.flight.validfields.FlightValidFields;
import com.bootcamp.desafioquality.service.validation.fields.PaymentMethodValidFields;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;

public class FlightReservationResponseDTOBuilder {
    public static final String SUCCESS_MESSAGE = "El proceso termino satisfactoriamente";
    private final FlightValidFields validFields;
    private double amount;
    private double total;
    private String flightNumber;
    private @Nullable String error = null;
    private HttpStatus status;


    public FlightReservationResponseDTOBuilder(FlightValidFields validFields) {
        this.validFields = validFields;
        this.status = HttpStatus.OK;
    }

    public FlightReservationResponseDTO build() {
        FlightReservationResponseDTO dto = new FlightReservationResponseDTO();
        dto.setUserName(validFields.getEmail());
        fillReservationDto(dto);
        StatusCodeDTO statusCode = new StatusCodeDTO();
        statusCode.setCode(status.value());
        if (error != null) {
            statusCode.setMessage(error);
        } else {
            fillPayment(dto);
            statusCode.setMessage(SUCCESS_MESSAGE);
        }
        dto.setStatusCode(statusCode);
        return dto;
    }

    private void fillPayment(FlightReservationResponseDTO dto) {
        PaymentMethodValidFields paymentMethodValidatedFields = validFields.getPaymentMethodValidatedFields();
        dto.setAmount(amount);
        dto.setTotal(total);
        dto.setInterest(paymentMethodValidatedFields.getInterest());
    }


    private void fillReservationDto(FlightReservationResponseDTO dto) {
        FlightReservationDTO flightReservation = new FlightReservationDTO();
        flightReservation.setFlightNumber(flightNumber)
                .setDateFrom(DateParser.toString(validFields.getDateFrom()))
                .setDateTo(DateParser.toString(validFields.getDateTo()))
                .setOrigin(validFields.getOrigin().getLabel())
                .setDestination(validFields.getDestination().getLabel())
                .setSeats(String.valueOf(validFields.getPeopleAmount()))
                .setSeatType(validFields.getSeatType().getLabel())
                .setPeople(validFields.getPersonValidatedFields().stream().map(PersonDTOBuilder::fromValidFields).collect(Collectors.toList()));        dto.setFlightReservation(flightReservation);
    }

    public FlightReservationResponseDTOBuilder withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public FlightReservationResponseDTOBuilder withTotal(double total) {
        this.total = total;
        return this;
    }

    public FlightReservationResponseDTOBuilder withFlightNumber(String code) {
        this.flightNumber = code;
        return this;
    }

    public void withError(String error) {
        this.error = error;
    }

    public void withStatus(HttpStatus status) {
        this.status = status;
    }
}
