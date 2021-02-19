package com.bootcamp.desafioquality.service.hotelroom.exception;

import com.bootcamp.desafioquality.exception.BadRequestException;

public class HotelRoomServiceException extends BadRequestException {

    public HotelRoomServiceException(String msg) {
        super(msg);
    }
}
