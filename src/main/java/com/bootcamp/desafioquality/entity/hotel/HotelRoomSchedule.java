package com.bootcamp.desafioquality.entity.hotel;

import com.bootcamp.desafioquality.date.DateRange;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotelRoomSchedule {
    private final List<DateRange> availableRanges;

    public HotelRoomSchedule(@NotNull Date dateFrom, @NotNull Date dateTo) {
        this.availableRanges = new ArrayList<>();
        availableRanges.add(new DateRange(dateFrom, dateTo)); // disponibilidad inicial
    }

    public void reserveRange(Date dateFrom, Date dateTo) {
        DateRange reqRange = new DateRange(dateFrom, dateTo);
        DateRange avlRange = availableRanges.stream()
                .filter(ar -> ar.isWithinRange(reqRange))
                .findFirst()
                .orElseThrow();
        adjustRanges(reqRange, avlRange);
    }

    private void adjustRanges(DateRange reqRange, DateRange avlRange) {
        if (avlRange.getDateFrom().equals(reqRange.getDateFrom())) { // si empiezan el mismo dia
            if (avlRange.getDateTo().equals(reqRange.getDateTo()))
                availableRanges.remove(avlRange); // toma el rango entero, lo saco entero
            else {
                // si llega acá es porque empiezan el mismo dia pero la reserva termina antes
                // entonces tengo que agregar un rango desde que termina la reserva hasta el to que habia
                // y al que habia cambiarle el to por el from de la reserva
                availableRanges.add(new DateRange(reqRange.getDateTo(), avlRange.getDateTo()));
                avlRange.setDateTo(reqRange.getDateFrom());
            }
        } else {
            // si llega aca es porque no empiezan el mismo dia
            if (avlRange.getDateTo().equals(reqRange.getDateTo())) {
                // si terminan el mismo dia tengo que acortar el avl hasta el from de la reserva
                avlRange.setDateTo(reqRange.getDateFrom());
            } else {
                // en este caso la reserva ocupa una porcion parcial en el medio
                // tengo que acortar el to del avl original al from de la reserva
                // tengo que insertar un nuevo avl desde el to de la reserva hasta el to original
                availableRanges.add(new DateRange(reqRange.getDateTo(), avlRange.getDateTo()));
            }
            avlRange.setDateTo(reqRange.getDateFrom());
        }
    }

    public boolean isRangeAvailable(Date dateFrom, Date dateTo) {
        DateRange dateRange = new DateRange(dateFrom, dateTo);
        return availableRanges.stream().anyMatch(dr -> dr.isWithinRange(dateRange));
    }

    // alguna fecha disponible
    public boolean hasDatesAvailable() {
        return !availableRanges.isEmpty();
    }

    public DateRange getNextAvailableRange() {
        return availableRanges.get(0);
    }

    public boolean isDateAvailable(Date date) {
        return availableRanges.stream().anyMatch(dr -> dr.isWithinRange(date));
    }
}
