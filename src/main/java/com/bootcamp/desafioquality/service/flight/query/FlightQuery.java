package com.bootcamp.desafioquality.service.flight.query;

import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.date.DateRangeValidator;
import com.bootcamp.desafioquality.entity.flight.Flight;
import com.bootcamp.desafioquality.entity.flight.SeatType;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.service.query.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.function.Function;

import static com.bootcamp.desafioquality.service.flight.query.FlightQueryParam.*;

public class FlightQuery extends Query<FlightQueryParam, Flight> {
    public static final Function<String, RuntimeException> EXCEPTION_SUPPLIER = FlightQueryException::new;
    private final DateRangeValidator dateRangeValidator;

    public FlightQuery() {
        this.dateRangeValidator = new DateRangeValidator(EXCEPTION_SUPPLIER);
    }

    public FlightQuery withDateFrom(@Nullable String dateFrom) {
        if (dateFrom != null) {
            Date date = DateParser.fromStringOrElseThrow(dateFrom, EXCEPTION_SUPPLIER);
            withDateFrom(date);
        }
        return this;
    }

    public FlightQuery withDateFrom(@NotNull Date date) {
        dateRangeValidator.validateDateFrom(date);
        filters.put(DATE_FROM, flight -> flight.getDateFrom().equals(date));
        return this;
    }

    public FlightQuery withDateTo(@NotNull Date date) {
        dateRangeValidator.validateDateTo(date);
        filters.put(DATE_TO, flight -> flight.getDateTo().equals(date));
        return this;
    }

    public FlightQuery withOrigin(@NotNull Location origin) {
        filters.put(ORIGIN, flight -> flight.getOrigin() == origin);
        return this;
    }

    public FlightQuery withDestination(@NotNull Location destination) {
        filters.put(DESTINATION, flight -> flight.getDestination() == destination);
        return this;
    }

    public FlightQuery withSeatType(@NotNull SeatType seatType) {
        filters.put(SEAT_TYPE, flight -> flight.getSeatType() == seatType);
        return this;
    }

    public FlightQuery withFlightNumber(@NotNull String flightNumber) {
        filters.put(FLIGHT_NUMBER, flight -> flight.getCode().equalsIgnoreCase(flightNumber));
        return this;
    }

    public FlightQuery withDateTo(@Nullable String dateTo) {
        if (dateTo != null) {
            Date date = DateParser.fromStringOrElseThrow(dateTo, FlightQueryException::new);
            withDateTo(date);
        }
        return this;
    }

    public FlightQuery withOrigin(@Nullable String origin) {
        if (origin != null) {
            Location location = Location.fromLabel(origin);
            withOrigin(location);
        }
        return this;
    }

    public FlightQuery withDestination(@Nullable String destination) {
        if (destination != null) {
            Location location = Location.fromLabel(destination);
            withDestination(location);
        }
        return this;
    }

    public void withSeatType(String seatType) {
        if (seatType != null) {
            SeatType type = SeatType.fromLabelOrElseThrow(seatType, EXCEPTION_SUPPLIER);
            withSeatType(type);
        }
    }

    public void withoutOrigin() {
        filters.remove(ORIGIN);
    }

    public void withoutDestination() {
        filters.remove(DESTINATION);
    }

    public void withoutDateFrom() {
        filters.remove(DATE_FROM);
    }

    public void withoutDateTo() {
        filters.remove(DATE_TO);
    }
}
