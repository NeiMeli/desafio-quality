package com.bootcamp.desafioquality.controller.flight.dto.request;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PaymentMethodDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.bootcamp.desafioquality.controller.dtoutil.Message.REQUIRED_FIELD;

public class FlightReservationDetailRequestDTO {
    @NotEmpty(message = REQUIRED_FIELD)
    private String dateFrom;

    @NotEmpty(message = REQUIRED_FIELD)
    private String dateTo;

    @NotEmpty(message = REQUIRED_FIELD)
    private String origin;

    @NotEmpty(message = REQUIRED_FIELD)
    private String destination;

    @NotEmpty(message = REQUIRED_FIELD)
    private String flightNumber;

    @NotEmpty(message = REQUIRED_FIELD)
    private String seats;

    @NotEmpty(message = REQUIRED_FIELD)
    private String seatType;

    @NotEmpty(message = REQUIRED_FIELD)
    private List<PersonDTO> people;

    @Valid
    @NotNull(message = REQUIRED_FIELD)
    private PaymentMethodDTO paymentMethod;

    public String getDateFrom() {
        return dateFrom;
    }

    public FlightReservationDetailRequestDTO setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public String getDateTo() {
        return dateTo;
    }

    public FlightReservationDetailRequestDTO setDateTo(String dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public String getOrigin() {
        return origin;
    }

    public FlightReservationDetailRequestDTO setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public FlightReservationDetailRequestDTO setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public FlightReservationDetailRequestDTO setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
        return this;
    }

    public String getSeats() {
        return seats;
    }

    public FlightReservationDetailRequestDTO setSeats(String seats) {
        this.seats = seats;
        return this;
    }

    public String getSeatType() {
        return seatType;
    }

    public FlightReservationDetailRequestDTO setSeatType(String seatType) {
        this.seatType = seatType;
        return this;
    }

    public List<PersonDTO> getPeople() {
        return people;
    }

    public FlightReservationDetailRequestDTO setPeople(List<PersonDTO> people) {
        this.people = people;
        return this;
    }

    public PaymentMethodDTO getPaymentMethod() {
        return paymentMethod;
    }

    public FlightReservationDetailRequestDTO setPaymentMethod(PaymentMethodDTO paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }
}
