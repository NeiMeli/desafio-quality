package com.bootcamp.desafioquality.repository.flight;

import com.bootcamp.desafioquality.entity.flight.Flight;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface FlightRepository {
    Stream<Flight> listWhere(Predicate<Flight> predicate);
    Optional<Flight> find(String flightNumber);
}
