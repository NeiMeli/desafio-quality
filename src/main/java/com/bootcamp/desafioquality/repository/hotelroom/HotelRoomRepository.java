package com.bootcamp.desafioquality.repository.hotelroom;

import com.bootcamp.desafioquality.entity.hotel.HotelRoom;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface HotelRoomRepository {
    Stream<HotelRoom> listWhere(Predicate<HotelRoom> predicate);

    Optional<HotelRoom> find(String code);
}
