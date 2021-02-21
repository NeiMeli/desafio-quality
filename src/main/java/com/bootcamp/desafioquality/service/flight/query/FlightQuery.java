package com.bootcamp.desafioquality.service.flight.query;

import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.date.DateRangeValidator;
import com.bootcamp.desafioquality.entity.flight.Flight;
import com.bootcamp.desafioquality.entity.flight.SeatType;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.service.query.Query;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

import static com.bootcamp.desafioquality.service.flight.query.FlightQueryParam.*;

public class FlightQuery extends Query<FlightQueryParam, Flight> {
    private final DateRangeValidator dateRangeValidator;

    public FlightQuery() {
        this.dateRangeValidator = new DateRangeValidator(FlightQueryException::new);
    }

    public FlightQuery withDateFrom(@Nullable String dateFrom) {
        if (dateFrom != null) {
            Date date = DateParser.fromStringOrElseThrow(dateFrom, FlightQueryException::new);
            dateRangeValidator.validateDateFrom(date);
            filters.put(DATE_FROM, flight -> flight.getDateFrom().equals(date));
        }
        return this;
    }

    public FlightQuery withDateTo(@Nullable String dateTo) {
        if (dateTo != null) {
            Date date = DateParser.fromStringOrElseThrow(dateTo, FlightQueryException::new);
            dateRangeValidator.validateDateTo(date);
            filters.put(DATE_TO, flight -> flight.getDateTo().equals(date));
        }
        return this;
    }

    public FlightQuery withOrigin(@Nullable String origin) {
        if (origin != null) {
            Location location = Location.fromLabel(origin);
            filters.put(ORIGIN, flight -> flight.getOrigin() == location);
        }
        return this;
    }

    public FlightQuery withDestination(@Nullable String destination) {
        if (destination != null) {
            Location location = Location.fromLabel(destination);
            filters.put(DESTINATION, flight -> flight.getDestination() == location);
        }
        return this;
    }

    public void withSeatType(String seatType) {
        if (seatType != null) {
            SeatType type = SeatType.fromLabel(seatType);
            filters.put(SEAT_TYPE, flight -> flight.getSeatType() == type);
        }
    }
}
