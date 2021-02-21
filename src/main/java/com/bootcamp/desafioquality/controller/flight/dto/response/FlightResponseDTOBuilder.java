package com.bootcamp.desafioquality.controller.flight.dto.response;

import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.entity.flight.Flight;

public class FlightResponseDTOBuilder {
    public static FlightResponseDTO build(Flight flight) {
        return new FlightResponseDTO()
                .setCode(flight.getCode())
                .setOrigin(flight.getOrigin().getLabel())
                .setDestination(flight.getDestination().getLabel())
                .setSeatType(flight.getSeatType().getLabel())
                .setPrice(flight.getPrice())
                .setDateFrom(DateParser.toString(flight.getDateFrom()))
                .setDateTo(DateParser.toString(flight.getDateTo()));
    }
}
