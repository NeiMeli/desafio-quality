package com.bootcamp.desafioquality.service.hotelroom.impl.query;

import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.service.query.Query;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQueryParam.*;

public class HotelRoomQuery extends Query<HotelRoomQueryParam, HotelRoom> {

    public HotelRoomQuery withDateTo(@Nullable String dateTo) {
        if (dateTo != null) {
            Date date = DateParser.fromString(dateTo);
            this.filters.put(DATE_TO, hr -> hr.getAvailableTo().compareTo(date) >= 0);
        }
        return this;
    }

    public HotelRoomQuery withDateFrom(@Nullable String dateFrom) {
        if (dateFrom != null) {
            Date date = DateParser.fromString(dateFrom);
            this.filters.put(DATE_FROM, hr -> hr.getAvailableFrom().compareTo(date) <= 0);
        }
        return this;
    }

    public HotelRoomQuery withDestinations(@Nullable String ... destinations) {
        if (destinations != null && destinations.length > 0) {
            List<String> destinationList = Arrays.asList(destinations);
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
