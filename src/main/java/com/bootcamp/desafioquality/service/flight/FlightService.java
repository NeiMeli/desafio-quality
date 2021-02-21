package com.bootcamp.desafioquality.service.flight;

import com.bootcamp.desafioquality.controller.flight.dto.request.FlightReservationRequestDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightResponseDTO;
import com.bootcamp.desafioquality.service.flight.query.FlightQuery;

import java.util.List;

public interface FlightService {
    List<FlightResponseDTO> query(FlightQuery query);
    FlightReservationResponseDTO reserveFlight(FlightReservationRequestDTO reservationDTO);
}
