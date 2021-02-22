package com.bootcamp.desafioquality.service.flight.impl;

import com.bootcamp.desafioquality.controller.flight.dto.request.FlightReservationRequestDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTOBuilder;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightResponseDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightResponseDTOBuilder;
import com.bootcamp.desafioquality.entity.flight.Flight;
import com.bootcamp.desafioquality.repository.flight.FlightRepository;
import com.bootcamp.desafioquality.service.flight.FlightService;
import com.bootcamp.desafioquality.service.flight.exception.FlightServiceError;
import com.bootcamp.desafioquality.service.flight.query.FlightQuery;
import com.bootcamp.desafioquality.service.flight.validfields.FlightValidFields;
import com.bootcamp.desafioquality.service.flight.validfields.FlightValidFieldsProcessor;
import com.bootcamp.desafioquality.service.validation.fields.PaymentMethodValidFields;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bootcamp.desafioquality.service.util.RoundUtil.*;

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
        String flightNumber = reservationDTO.getFlightReservation().getFlightNumber();
        FlightReservationResponseDTOBuilder responseBuilder = new FlightReservationResponseDTOBuilder(validFields);
        final Optional<Flight> flightOpt = findFlight(flightNumber, validFields);
        if (flightOpt.isEmpty()) {
            responseBuilder.withStatus(HttpStatus.NOT_FOUND);
            responseBuilder.withError(FlightServiceError.FLIGHT_NOT_FOUND.getMessage());
            return responseBuilder.build();
        }
        Flight flight = flightOpt.get();
        double amount = calculateAmount(flight.getPrice(), validFields.getPeopleAmount());
        double total = calculateTotal(amount, validFields.getPaymentMethodValidatedFields());
        responseBuilder
                .withFlightNumber(flight.getCode())
                .withAmount(amount)
                .withTotal(total);
        return responseBuilder.build();
    }

    private double calculateTotal(double amount, PaymentMethodValidFields paymentMethodValidatedFields) {
        double interest = paymentMethodValidatedFields.getInterest();
        return roundTwoDecimals(amount * (1 + interest / 100));
    }

    private double calculateAmount(double price, Integer peopleAmount) {
        return roundTwoDecimals(price * peopleAmount);
    }

    private Optional<Flight> findFlight(@NotNull String flightNumber, FlightValidFields validFields) {
        FlightQuery query = new FlightQuery()
                .withFlightNumber(flightNumber)
                .withDateFrom(validFields.getDateFrom())
                .withDateTo(validFields.getDateTo())
                .withOrigin(validFields.getOrigin())
                .withDestination(validFields.getDestination());
        return repository.listWhere(query.buildPredicate()).findFirst();
    }
}
