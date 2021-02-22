package com.bootcamp.desafioquality.controller.flight.dto.response;

import com.bootcamp.desafioquality.controller.flight.dto.FlightReservationDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PaymentMethodDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.service.flight.validfields.FlightValidFields;
import com.bootcamp.desafioquality.service.validation.fields.PaymentMethodValidFields;
import com.bootcamp.desafioquality.service.validation.fields.PersonValidFields;

import java.util.ArrayList;
import java.util.List;

public class FlightReservationResponseDTOBuilder {
    private final FlightValidFields validFields;
    private double amount;
    private double total;
    private String flightNumber;

    public FlightReservationResponseDTOBuilder(FlightValidFields validFields) {
        this.validFields = validFields;
    }

    public FlightReservationResponseDTO build() {
        FlightReservationResponseDTO dto = new FlightReservationResponseDTO();
        dto.setUserName(validFields.getEmail());
        fillReservationDto(dto);
        fillPeople(dto);
        fillPayment(dto);
        return dto;
    }

    private void fillPayment(FlightReservationResponseDTO dto) {
        PaymentMethodDTO paymentMethod = new PaymentMethodDTO();
        PaymentMethodValidFields paymentMethodValidatedFields = validFields.getPaymentMethodValidatedFields();
        paymentMethod.setDues(paymentMethodValidatedFields.getInstallments());
        paymentMethod.setType(paymentMethodValidatedFields.getPaymentMethodType().getLabel());
        dto.getFlightReservation().setPaymentMethod(paymentMethod);
        dto.setAmount(amount);
        dto.setTotal(total);
        dto.setInterest(paymentMethodValidatedFields.getInterest());
    }

    private void fillPeople(FlightReservationResponseDTO dto) {
        List<PersonDTO> people = new ArrayList<>();
        List<PersonValidFields> personValidatedFields = validFields.getPersonValidatedFields();
        personValidatedFields.forEach(p -> {
            PersonDTO personDTO = new PersonDTO()
                    .setMail(p.getMail())
                    .setDni(p.getDni())
                    .setName(p.getName())
                    .setLastName(p.getLastName())
                    .setBirthDate(p.getBirthDate());
            people.add(personDTO);
        });
        dto.getFlightReservation().setPeople(people);
    }

    private void fillReservationDto(FlightReservationResponseDTO dto) {
        FlightReservationDTO flightReservation = new FlightReservationDTO();
        flightReservation.setFlightNumber(flightNumber)
                .setDateFrom(DateParser.toString(validFields.getDateFrom()))
                .setDateTo(DateParser.toString(validFields.getDateTo()))
                .setOrigin(validFields.getOrigin().getLabel())
                .setDestination(validFields.getDestination().getLabel())
                .setSeats(String.valueOf(validFields.getPeopleAmount()))
                .setSeatType(validFields.getSeatType().getLabel());
        dto.setFlightReservation(flightReservation);
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
}
