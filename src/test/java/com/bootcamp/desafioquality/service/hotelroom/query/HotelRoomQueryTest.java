package com.bootcamp.desafioquality.service.hotelroom.query;

import com.bootcamp.desafioquality.date.DateRangeValidator;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQuery;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQueryException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class HotelRoomQueryTest {
    @Test
    void testQueryBadRequests() {
        HotelRoomQuery hotelRoomQuery = new HotelRoomQuery();
        // ubicacion invalida
        String invalidLocation = "non-existent-destination";
        assertThatExceptionOfType(Location.LocationNotFoundException.class)
                .isThrownBy(() -> hotelRoomQuery.withDestinations(invalidLocation))
                .withMessageContaining(Location.LocationNotFoundException.MESSAGE, invalidLocation);

        // fecha hasta invalida
        hotelRoomQuery.withDateFrom("12/02/2021");
        String invalidDateTo = "11/02/2021";
        assertThatExceptionOfType(HotelRoomQueryException.class)
                .isThrownBy(() -> hotelRoomQuery.withDateTo(invalidDateTo))
                .withMessageContaining(DateRangeValidator.DateRangeError.INVALID_DATE_TO.getMessage());

        // fecha desde invalida
        hotelRoomQuery.withDateTo("14/02/2021");
        String invalidDateFrom = "14/02/2021";
        assertThatExceptionOfType(HotelRoomQueryException.class)
                .isThrownBy(() -> hotelRoomQuery.withDateFrom(invalidDateFrom))
                .withMessageContaining(DateRangeValidator.DateRangeError.INVALID_DATE_FROM.getMessage());
    }
}
