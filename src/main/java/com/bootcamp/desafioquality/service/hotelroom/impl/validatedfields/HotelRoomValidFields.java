package com.bootcamp.desafioquality.service.hotelroom.impl.validatedfields;

import com.bootcamp.desafioquality.entity.hotel.RoomType;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.service.validation.fields.CommonValidFields;

import java.util.function.Function;

public class HotelRoomValidFields extends CommonValidFields {
    private RoomType roomType;
    private Location location;

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

    public Location getLocation() {
        ensureFieldWasValidated(location, "Ubicacion");
        return location;
    }

    public CommonValidFields setLocation(Location location) {
        this.location = location;
        return this;
    }

}
