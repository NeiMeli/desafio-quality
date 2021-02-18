package com.bootcamp.desafioquality.controller.hotelroom.dto.response;

import com.bootcamp.desafioquality.entity.hotel.HotelRoom;

public class HotelRoomResponseDTOBuilder {
    public static HotelRoomResponseDTO build (HotelRoom hotelRoom) {
        return new HotelRoomResponseDTO()
                .setCode(hotelRoom.getCode())
                .setHotelName(hotelRoom.getHotelName())
                .setLocation(hotelRoom.getLocation());
    }
}
