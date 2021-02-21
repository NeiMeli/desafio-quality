package com.bootcamp.desafioquality.service.hotelroom.impl.validfields;

import com.bootcamp.desafioquality.entity.hotel.RoomType;
import com.bootcamp.desafioquality.service.validation.fields.CommonValidFields;

import java.util.function.Function;

public class HotelRoomValidFields extends CommonValidFields {
    private RoomType roomType;

    public HotelRoomValidFields(Function<String, RuntimeException> exceptionSupplier) {
        super(exceptionSupplier);
    }

    public RoomType getRoomType() {
        return roomType;
    }

    protected HotelRoomValidFields setRoomType(RoomType roomType) {
        this.roomType = roomType;
        return this;
    }

    public boolean hasRoomType() {
        return roomType != null;
    }

}
