package com.bootcamp.desafioquality.controller.flight.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.bootcamp.desafioquality.controller.dtoutil.Message.*;

public class FlightReservationRequestDTO {

    @NotEmpty(message = REQUIRED_FIELD)
    private String userName;

    @Valid
    @NotNull(message = REQUIRED_FIELD)
    private FlightReservationDetailRequestDTO flightReservation;

    public String getUserName() {
        return userName;
    }

    public FlightReservationRequestDTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public FlightReservationDetailRequestDTO getFlightReservation() {
        return flightReservation;
    }

    public FlightReservationRequestDTO setFlightReservation(FlightReservationDetailRequestDTO flightReservation) {
        this.flightReservation = flightReservation;
        return this;
    }
}
