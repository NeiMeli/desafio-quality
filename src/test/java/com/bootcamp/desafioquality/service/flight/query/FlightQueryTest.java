package com.bootcamp.desafioquality.service.flight.query;

import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.date.DateRangeValidator;
import com.bootcamp.desafioquality.entity.flight.SeatType;
import com.bootcamp.desafioquality.entity.location.Location;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class FlightQueryTest {
    @Test
    void testQueryBadRequests() {
        FlightQuery query = new FlightQuery();

        // ubicacion invalida
        String invalidLocation = "non-existent-destination";
        assertThatExceptionOfType(Location.LocationNotFoundException.class)
                .isThrownBy(() -> query.withDestination(invalidLocation))
                .withMessageContaining(Location.LocationNotFoundException.MESSAGE, invalidLocation);
        assertThatExceptionOfType(Location.LocationNotFoundException.class)
                .isThrownBy(() -> query.withOrigin(invalidLocation))
                .withMessageContaining(Location.LocationNotFoundException.MESSAGE, invalidLocation);

        // formato de fecha invalido
        final String invalidDateFormat = "12//022";
        assertThatExceptionOfType(FlightQueryException.class)
                .isThrownBy(() -> query.withDateFrom(invalidDateFormat))
                .withMessageContaining(String.format(DateParser.ERROR_MESSAGE, invalidDateFormat));
        assertThatExceptionOfType(FlightQueryException.class)
                .isThrownBy(() -> query.withDateTo(invalidDateFormat))
                .withMessageContaining(String.format(DateParser.ERROR_MESSAGE, invalidDateFormat));
        query.withDateFrom("12/02/2021");

        // Rango invalido
        // fecha hasta invalida
        query.withDateFrom("12/02/2021");
        String invalidDateTo = "11/02/2021";
        assertThatExceptionOfType(FlightQueryException.class)
                .isThrownBy(() -> query.withDateTo(invalidDateTo))
                .withMessageContaining(DateRangeValidator.DateRangeError.INVALID_DATE_TO.getMessage());
        // fecha desde invalida
        query.withDateTo("14/02/2021");
        String invalidDateFrom = "14/02/2021";
        assertThatExceptionOfType(FlightQueryException.class)
                .isThrownBy(() -> query.withDateFrom(invalidDateFrom))
                .withMessageContaining(DateRangeValidator.DateRangeError.INVALID_DATE_FROM.getMessage());

        // tipo de asiento invalido
        final String invalidSeatType = "not-a-seat-type";
        assertThatExceptionOfType(FlightQueryException.class)
                .isThrownBy(() -> query.withSeatType(invalidSeatType))
                .withMessageContaining(String.format(SeatType.SeatTypeNotFoundException.MESSAGE, invalidSeatType));
    }
}
