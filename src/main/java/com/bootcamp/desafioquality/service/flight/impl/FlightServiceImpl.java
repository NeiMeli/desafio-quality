package com.bootcamp.desafioquality.service.flight.impl;

import com.bootcamp.desafioquality.controller.flight.dto.request.FlightReservationRequestDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightResponseDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightResponseDTOBuilder;
import com.bootcamp.desafioquality.repository.flight.FlightRepository;
import com.bootcamp.desafioquality.service.flight.FlightService;
import com.bootcamp.desafioquality.service.flight.query.FlightQuery;
import com.bootcamp.desafioquality.service.flight.validfields.FlightValidFields;
import com.bootcamp.desafioquality.service.flight.validfields.FlightValidFieldsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService  {
    @Autowired
    FlightRepository repository;

    @Override
    public List<FlightResponseDTO> query(FlightQuery query) {
        return repository.listWhere(query.buildPredicate())
                .map(FlightResponseDTOBuilder::build)
                .collect(Collectors.toList());
    }

    @Override
    public FlightReservationResponseDTO reserveFlight(FlightReservationRequestDTO reservationDTO) {
        FlightValidFields validFields = new FlightValidFieldsProcessor().validate(reservationDTO);
        return null;
    }
}
