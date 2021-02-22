package com.bootcamp.desafioquality.repository.flight.impl;

import com.bootcamp.desafioquality.entity.flight.Flight;
import com.bootcamp.desafioquality.exception.BadRequestException;
import com.bootcamp.desafioquality.repository.CacheDBTable;
import com.bootcamp.desafioquality.repository.CacheRepository;
import com.bootcamp.desafioquality.repository.flight.FlightRepository;
import com.bootcamp.desafioquality.repository.util.JsonDBUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Repository
public class FlightCacheRepository implements FlightRepository, CacheRepository<Integer, Flight> {
    final CacheDBTable<Integer, Flight> database;

    public FlightCacheRepository() throws Exception {
        database = new CacheDBTable<>() {
            @Override
            protected @NotNull Integer generateNextId() {
                return size() + 1;
            }
        };
        JsonNode jsonNodes = JsonDBUtil.parseDatabase("src/main/resources/database/json/flights.json");
        for (JsonNode jsonNode : jsonNodes) {
            database.persist(Flight.fromJson(jsonNode));
        }
    }
    @Override
    public CacheDBTable<Integer, Flight> getDatabase() {
        return this.database;
    }

    @Override
    public Stream<Flight> listWhere(Predicate<Flight> predicate) {
        return getDatabase().listWhere(predicate);
    }
}
