package com.bootcamp.desafioquality.service.hotelroom.impl.query;

import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.date.DateRangeValidator;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.entity.hotel.RoomType;
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
    private Date dateFrom = null;
    private Date dateTo = null;

    public HotelRoomQuery() {
        this.dateRangeValidator = new DateRangeValidator(HotelRoomQueryException::new);
    }

    public HotelRoomQuery withDateTo(@Nullable String dateTo) {
        if (dateTo != null) {
            Date date = DateParser.fromStringOrElseThrow(dateTo, HotelRoomQueryException::new);
            withDateTo(date);
        }
        return this;
    }

    public HotelRoomQuery withDateTo(@NotNull Date date) {
        dateRangeValidator.validateDateTo(date);
        this.dateTo = date;
        return this;
    }

    public HotelRoomQuery withDateFrom(@Nullable String dateFrom) {
        if (dateFrom != null) {
            Date date = DateParser.fromStringOrElseThrow(dateFrom, HotelRoomQueryException::new);
            withDateFrom(date);
        }
        return this;
    }

    public HotelRoomQuery withDateFrom(@NotNull Date date) {
        dateRangeValidator.validateDateFrom(date);
        this.dateFrom = date;
        return this;
    }

    public HotelRoomQuery withDestinations(@Nullable String ... destinations) {
        if (destinations != null && destinations.length > 0) {
            List<Location> destinationList = Arrays.stream(destinations).map(Location::fromLabel).collect(Collectors.toList());
            withDestinations(destinationList);
        }
        return this;
    }

    public HotelRoomQuery withDestinations(List<Location> destinations) {
        filters.put(DESTINATION, hr -> destinations.contains(hr.getLocation()));
        return this;
    }

    public HotelRoomQuery withAnyAvailability() {
        filters.put(AVAILABILITY, HotelRoom::hasDatesAvailable);
        return this;
    }

    public HotelRoomQuery withoutDestinations() {
        filters.remove(DESTINATION);
        return this;
    }

    public HotelRoomQuery withoutDateTo() {
        this.dateTo = null;
        dateRangeValidator.withoutDateTo();
        return this;
    }


    public HotelRoomQuery withoutDateFrom() {
        this.dateFrom = null;
        dateRangeValidator.withoutDateFrom();
        return this;
    }

    @Override
    public Predicate<HotelRoom> buildPredicate() {
        return hr -> {
            Predicate<HotelRoom> pr = super.buildPredicate();
            if (dateFrom != null && dateTo != null) {
                return pr.test(hr) && hr.hasRangeAvailable(dateFrom, dateTo);
            } else {
                if (dateFrom != null)
                    return pr.test(hr) && hr.hasDateAvailable(dateFrom);
                else if (dateTo != null) return pr.test(hr) && hr.hasDateAvailable(dateTo);
                else return pr.test(hr);
            }
        };
    }

    public void withRoomType(RoomType roomType) {
        filters.put(ROOM_TYPE, hr -> hr.getRoomType() == roomType);
    }

    public void withHotelCode(@NotNull String hotelCode) {
        filters.put(HOTEL_CODE, hr -> hr.getCode().equals(hotelCode));
    }
}
