package com.bootcamp.desafioquality.controller.flight.dto.response;

import com.bootcamp.desafioquality.entity.flight.Flight;

public class FlightResponseDTOBuilder {
    public static FlightResponseDTO build(Flight flight) {
        return new FlightResponseDTO()
                .setCode(flight.getCode())
                .setOrigin(flight.getOrigin())
                .setDestination(flight.getDestination())
                .setSeatType(flight.getSeatType())
                .setPrice(flight.getPrice())
                .setDateFrom(flight.getDateFrom())
                .setDateTo(flight.getDateTo());
    }
}
