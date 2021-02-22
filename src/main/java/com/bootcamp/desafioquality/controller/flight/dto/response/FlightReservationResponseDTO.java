package com.bootcamp.desafioquality.controller.flight.dto.response;

import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.flight.dto.FlightReservationDTO;

public class FlightReservationResponseDTO {
    private String userName;
    private Double amount;
    private Double interest;
    private Double total;
    private FlightReservationDTO flightReservation;
    private StatusCodeDTO statusCode;

    public String getUserName() {
        return userName;
    }

    public FlightReservationResponseDTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public FlightReservationResponseDTO setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public Double getInterest() {
        return interest;
    }

    public FlightReservationResponseDTO setInterest(Double interest) {
        this.interest = interest;
        return this;
    }

    public Double getTotal() {
        return total;
    }

    public FlightReservationResponseDTO setTotal(Double total) {
        this.total = total;
        return this;
    }

    public FlightReservationDTO getFlightReservation() {
        return flightReservation;
    }

    public FlightReservationResponseDTO setFlightReservation(FlightReservationDTO flightReservation) {
        this.flightReservation = flightReservation;
        return this;
    }

    public StatusCodeDTO getStatusCode() {
        return statusCode;
    }

    public FlightReservationResponseDTO setStatusCode(StatusCodeDTO statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}
