package com.bootcamp.desafioquality.repository.hotelroom.impl;

import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.exception.BadRequestException;
import com.bootcamp.desafioquality.repository.CacheDBTable;
import com.bootcamp.desafioquality.repository.CacheRepository;
import com.bootcamp.desafioquality.repository.hotelroom.HotelRoomRepository;
import com.bootcamp.desafioquality.repository.util.JsonDBUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Repository
public class HotelRoomCacheRespository implements HotelRoomRepository, CacheRepository<String, HotelRoom> {
    final CacheDBTable<String, HotelRoom> database;

    public HotelRoomCacheRespository() throws Exception {
        database = new CacheDBTable<>() {
            @Override
            protected @NotNull String generateNextId() {
                throw new BadRequestException("No se puede guardar un hotel sin código!");
            }
        };
        JsonNode jsonNodes = JsonDBUtil.parseDatabase("src/main/resources/database/json/hotel-rooms.json");
        for (JsonNode jsonNode : jsonNodes) {
            database.persist(HotelRoom.fromJson(jsonNode));
        }
    }

    @Override
    public CacheDBTable<String, HotelRoom> getDatabase() {
        return this.database;
    }

    @Override
    public Stream<HotelRoom> listWhere(Predicate<HotelRoom> predicate) {
        return database.listWhere(predicate);
    }
}
