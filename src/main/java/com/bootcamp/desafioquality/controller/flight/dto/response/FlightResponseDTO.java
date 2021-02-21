package com.bootcamp.desafioquality.controller.flight.dto.response;

import com.bootcamp.desafioquality.entity.flight.SeatType;
import com.bootcamp.desafioquality.entity.location.Location;

import java.util.Date;

public class FlightResponseDTO {
    private String code;
    private Location origin;
    private Location destination;
    private SeatType seatType;
    private Double price;
    private Date dateFrom;
    private Date dateTo;

    public String getCode() {
        return code;
    }

    public FlightResponseDTO setCode(String code) {
        this.code = code;
        return this;
    }

    public Location getOrigin() {
        return origin;
    }

    public FlightResponseDTO setOrigin(Location origin) {
        this.origin = origin;
        return this;
    }

    public Location getDestination() {
        return destination;
    }

    public FlightResponseDTO setDestination(Location destination) {
        this.destination = destination;
        return this;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public FlightResponseDTO setSeatType(SeatType seatType) {
        this.seatType = seatType;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public FlightResponseDTO setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public FlightResponseDTO setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public FlightResponseDTO setDateTo(Date dateTo) {
        this.dateTo = dateTo;
        return this;
    }
}
