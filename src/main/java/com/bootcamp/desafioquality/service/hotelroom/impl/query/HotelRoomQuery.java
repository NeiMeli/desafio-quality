package com.bootcamp.desafioquality.service.hotelroom.impl.query;

import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.date.DateRangeValidator;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.service.query.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQueryParam.*;

public class HotelRoomQuery extends Query<HotelRoomQueryParam, HotelRoom> {
    private final DateRangeValidator dateRangeValidator;

    public HotelRoomQuery() {
        this.dateRangeValidator = new DateRangeValidator(HotelRoomQueryException::new);
    }

    // todo acá hay un bug. tengo que chequear que esté BETWEEN el availableFrom y el To del hotel.
    public HotelRoomQuery withDateTo(@Nullable String dateTo) {
        if (dateTo != null) {
            Date date = DateParser.fromString(dateTo);
            dateRangeValidator.validateDateTo(date);
            this.filters.put(DATE_TO, betweenAvailabilityRange(date));
        }
        return this;
    }

    private Predicate<HotelRoom> betweenAvailabilityRange(@NotNull Date date) {
        return hr -> hr.getAvailableFrom().compareTo(date) <= 0 && hr.getAvailableTo().compareTo(date) >= 0;
    }


    // todo acá hay un bug. tengo que chequear que esté BETWEEN el availableFrom y el To del hotel.
    public HotelRoomQuery withDateFrom(@Nullable String dateFrom) {
        if (dateFrom != null) {
            Date date = DateParser.fromString(dateFrom);
            dateRangeValidator.validateDateFrom(date);
            this.filters.put(DATE_FROM, betweenAvailabilityRange(date));
        }
        return this;
    }

    public HotelRoomQuery withDestinations(@Nullable String ... destinations) {
        if (destinations != null && destinations.length > 0) {
            List<Location> destinationList = Arrays.stream(destinations).map(Location::fromLabel).collect(Collectors.toList());
            filters.put(DESTINATION, hr -> destinationList.contains(hr.getLocation()));
        }
        return this;
    }

    public HotelRoomQuery withAvailability() {
        filters.put(AVAILABILITY, HotelRoom::isAvailable);
        return this;
    }

    public HotelRoomQuery withoutDestinations() {

        filters.remove(DESTINATION);
        return this;
    }

    public HotelRoomQuery withoutDateTo() {
        filters.remove(DATE_TO);
        dateRangeValidator.withoutDateTo();
        return this;
    }


    public HotelRoomQuery withoutDateFrom() {
        filters.remove(DATE_FROM);
        dateRangeValidator.withoutDateFrom();
        return this;
    }
}
