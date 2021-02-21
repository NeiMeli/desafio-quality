package com.bootcamp.desafioquality.service.flight.validfields;

import com.bootcamp.desafioquality.entity.flight.SeatType;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.service.validation.fields.CommonValidFields;

import java.util.function.Function;

public class FlightValidFields extends CommonValidFields {
    private Location origin;
    private Location destination;
    private SeatType seatType;

    public FlightValidFields(Function<String, RuntimeException> exceptionSupplier) {
        super(exceptionSupplier);
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public void setSeatType(SeatType st) {
        this.seatType = st;
    }
}
