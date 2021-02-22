package com.bootcamp.desafioquality.controller.hotelroom.dto.response;

import com.bootcamp.desafioquality.date.DateRange;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;

import java.util.stream.Collectors;

public class HotelRoomResponseDTOBuilder {
    public static HotelRoomResponseDTO build (HotelRoom hotelRoom) {
        return new HotelRoomResponseDTO()
                .setCode(hotelRoom.getCode())
                .setHotelName(hotelRoom.getHotelName())
                .setPrice(hotelRoom.getPrice())
                .setLocation(hotelRoom.getLocation().getLabel())
                .setAvailableDateRanges(hotelRoom.getAvailableDateRanges().stream().map(DateRange::describe).collect(Collectors.toList()));
    }
}
