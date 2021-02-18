package com.bootcamp.desafioquality.service.hotelroom.impl.query;

import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.service.query.Query;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQueryException.HotelRoomQueryExceptionMessage.*;
import static com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQueryParam.*;

public class HotelRoomQuery extends Query<HotelRoomQueryParam, HotelRoom> {

    private Predicate<Date> dateFromValidation = dateFrom -> true;
    private Predicate<Date> dateToValidation = dateTo -> true;

    public HotelRoomQuery withDateTo(@Nullable String dateTo) {
        if (dateTo != null) {
            Date date = DateParser.fromString(dateTo);
            validateDateTo(date, dateTo);
            dateFromValidation = dateFrom -> date.compareTo(dateFrom) > 0;
            this.filters.put(DATE_TO, hr -> hr.getAvailableTo().compareTo(date) >= 0);
        }
        return this;
    }

    private void validateDateTo(Date dateTo, String dateToString) {
        if (!dateToValidation.test(dateTo)) {
            throw new HotelRoomQueryException(INVALID_DATE_TO.getMessage());
        }
    }

    public HotelRoomQuery withDateFrom(@Nullable String dateFrom) {
        if (dateFrom != null) {
            Date date = DateParser.fromString(dateFrom);
            validateDateFrom(date, dateFrom);
            dateToValidation = dateTo -> date.compareTo(dateTo) < 0;
            this.filters.put(DATE_FROM, hr -> hr.getAvailableFrom().compareTo(date) <= 0);
        }
        return this;
    }

    private void validateDateFrom(Date dateFrom, String dateFromString) {
        if (!dateFromValidation.test(dateFrom)) {
            throw new HotelRoomQueryException(INVALID_DATE_FROM.getMessage());
        }
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
        return this;
    }


    public HotelRoomQuery withoutDateFrom() {
        filters.remove(DATE_FROM);
        return this;
    }
}
