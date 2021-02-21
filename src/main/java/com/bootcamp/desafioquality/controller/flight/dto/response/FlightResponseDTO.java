package com.bootcamp.desafioquality.controller.flight.dto.response;

public class FlightResponseDTO {
    private String code;
    private String origin;
    private String destination;
    private String seatType;
    private Double price;
    private String dateFrom;
    private String dateTo;

    public String getCode() {
        return code;
    }

    public FlightResponseDTO setCode(String code) {
        this.code = code;
        return this;
    }

    public String getOrigin() {
        return origin;
    }

    public FlightResponseDTO setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public FlightResponseDTO setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getSeatType() {
        return seatType;
    }

    public FlightResponseDTO setSeatType(String seatType) {
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

    public String getDateFrom() {
        return dateFrom;
    }

    public FlightResponseDTO setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public String getDateTo() {
        return dateTo;
    }

    public FlightResponseDTO setDateTo(String dateTo) {
        this.dateTo = dateTo;
        return this;
    }
}
