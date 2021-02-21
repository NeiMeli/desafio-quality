package com.bootcamp.desafioquality.service.hotelroom.impl.query;

import com.bootcamp.desafioquality.exception.BadRequestException;

public class HotelRoomQueryException extends BadRequestException {
    public HotelRoomQueryException(String message) {
        super(message);
    }
}
