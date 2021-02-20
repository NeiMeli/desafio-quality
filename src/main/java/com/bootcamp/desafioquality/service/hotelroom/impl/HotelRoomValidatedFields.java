package com.bootcamp.desafioquality.service.hotelroom.impl;

import com.bootcamp.desafioquality.entity.hotel.RoomType;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.service.validation.ValidatedFields;

import java.util.function.Function;

public class HotelRoomValidatedFields extends ValidatedFields {
    private RoomType roomType;
    private Location location;

    public HotelRoomValidatedFields(Function<String, RuntimeException> exceptionSupplier) {
        super(exceptionSupplier);
    }

    public RoomType getRoomType() {
        return roomType;
    }

    protected HotelRoomValidatedFields setRoomType(RoomType roomType) {
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

    public ValidatedFields setLocation(Location location) {
        this.location = location;
        return this;
    }

}
