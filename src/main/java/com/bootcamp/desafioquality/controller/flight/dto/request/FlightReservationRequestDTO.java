package com.bootcamp.desafioquality.controller.flight.dto.request;

import com.bootcamp.desafioquality.controller.flight.dto.FlightReservationDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.bootcamp.desafioquality.controller.dtoutil.Message.*;

public class FlightReservationRequestDTO {

    @NotEmpty(message = REQUIRED_FIELD)
    private String userName;

    @NotNull(message = REQUIRED_FIELD)
    private FlightReservationDTO flightReservation;

    public String getUserName() {
        return userName;
    }

    public FlightReservationRequestDTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public FlightReservationDTO getFlightReservation() {
        return flightReservation;
    }

    public FlightReservationRequestDTO setFlightReservation(FlightReservationDTO flightReservation) {
        this.flightReservation = flightReservation;
        return this;
    }
}
