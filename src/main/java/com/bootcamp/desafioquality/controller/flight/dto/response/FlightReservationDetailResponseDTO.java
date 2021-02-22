package com.bootcamp.desafioquality.controller.flight.dto.response;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightReservationDetailResponseDTO {
    private String dateFrom;
    private String dateTo;
    private String origin;
    private String destination;
    private String flightNumber;
    private String seats;
    private String seatType;
    private List<PersonDTO> people;

    public String getDateFrom() {
        return dateFrom;
    }

    public FlightReservationDetailResponseDTO setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public String getDateTo() {
        return dateTo;
    }

    public FlightReservationDetailResponseDTO setDateTo(String dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public String getOrigin() {
        return origin;
    }

    public FlightReservationDetailResponseDTO setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public FlightReservationDetailResponseDTO setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public FlightReservationDetailResponseDTO setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
        return this;
    }

    public String getSeats() {
        return seats;
    }

    public FlightReservationDetailResponseDTO setSeats(String seats) {
        this.seats = seats;
        return this;
    }

    public String getSeatType() {
        return seatType;
    }

    public FlightReservationDetailResponseDTO setSeatType(String seatType) {
        this.seatType = seatType;
        return this;
    }

    public List<PersonDTO> getPeople() {
        return people;
    }

    public FlightReservationDetailResponseDTO setPeople(List<PersonDTO> people) {
        this.people = people;
        return this;
    }
}
