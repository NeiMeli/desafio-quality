package com.bootcamp.desafioquality.common;

import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.repository.util.JsonDBUtil;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class HotelRoomTestConstants {
    // base de datos mockeada y renovada en cada test para asegurar la independencia
    public static final Supplier<List<HotelRoom>> DATABASE;
    static {
        DATABASE = () -> {
            final List<HotelRoom> hotelRoomList = new ArrayList<>();
            JsonNode jsonNodes = null;
            try {
                jsonNodes = JsonDBUtil.parseDatabase("src/main/resources/database/json/hotel-rooms.json");
            } catch (Exception e) {
                // ignore);
            }
            if (jsonNodes != null) {
                for (JsonNode jsonNode : jsonNodes) {
                    hotelRoomList.add(HotelRoom.fromJson(jsonNode));
                }
            }
            return hotelRoomList;
        };
    }
}
