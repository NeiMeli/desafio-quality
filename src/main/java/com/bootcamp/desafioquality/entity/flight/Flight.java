package com.bootcamp.desafioquality.entity.flight;

import com.bootcamp.desafioquality.entity.Persistable;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Flight implements Persistable<String> {
    private String code;
    private String origin;
    private String destination;
    private SeatType seatType;
    private double price;
    private Date dateFrom;
    private Date dateTo;

    public static Flight fromJson(JsonNode jn) {
        Flight flight = new Flight();
        flight.setCode(jn.get("nroVuelo").textValue())
                .setOrigin(jn.get("origen").textValue())
                .setDestination(jn.get("destino").textValue())
                .setSeatType(SeatType.fromLabel(jn.get("tipoAsiento").textValue()))
                .setPrice(jn.get("precioPorPersona").doubleValue())
                .setDateFrom(jn.get("fechaIda").textValue())
                .setDateTo(jn.get("fechaVuelta").textValue());
        return flight;
    }

    public String getCode() {
        return code;
    }

    public Flight setCode(String code) {
        this.code = code;
        return this;
    }

    public String getOrigin() {
        return origin;
    }

    public Flight setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public Flight setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public Flight setSeatType(SeatType seatType) {
        this.seatType = seatType;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public Flight setPrice(double price) {
        this.price = price;
        return this;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Flight setDateFrom(String dateFrom) {
        try {
            this.dateFrom = new SimpleDateFormat("dd/MM/yyyy").parse(dateFrom);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public Flight setDateTo(String dateTo) {
        try {
            this.dateTo = new SimpleDateFormat("dd/MM/yyyy").parse(dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public String getPrimaryKey() {
        return this.code;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public void setId(@NotNull String id) {
        setCode(id);
    }
}
