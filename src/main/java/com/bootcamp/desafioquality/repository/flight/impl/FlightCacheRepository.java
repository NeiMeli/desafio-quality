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

@Repository
public class FlightCacheRepository implements FlightRepository, CacheRepository<String, Flight> {
    final CacheDBTable<String, Flight> database;

    public FlightCacheRepository() throws Exception {
        database = new CacheDBTable<>() {
            @Override
            protected @NotNull String generateNextId() {
                throw new BadRequestException("No se puede guardar un vuelo sin código!");
            }
        };
        JsonNode jsonNodes = JsonDBUtil.parseDatabase("src/main/resources/database/json/flights.json");
        for (JsonNode jsonNode : jsonNodes) {
            database.persist(Flight.fromJson(jsonNode));
        }
    }
    @Override
    public CacheDBTable<String, Flight> getDatabase() {
        return this.database;
    }
}
