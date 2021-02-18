package com.bootcamp.desafioquality.entity.hotel;

import com.bootcamp.desafioquality.entity.Persistable;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotelRoom implements Persistable<String> {
    private String code;
    private String hotelName;
    private String location;
    private RoomType roomType;
    private double price;
    private Date availableFrom;
    private Date availableTo;
    private boolean reserved;

    public static HotelRoom fromJson(JsonNode jn) {
        HotelRoom hotelRoom = new HotelRoom();
        hotelRoom.setCode(jn.get("codigoHotel").textValue())
                .setHotelName(jn.get("nombre").textValue())
                .setLocation(jn.get("lugar/Ciudad").textValue())
                .setRoomType(RoomType.fromLabel(jn.get("tipoDeHabitacion").textValue()))
                .setAvailableFrom(jn.get("disponibleDesde").textValue())
                .setAvailableTo(jn.get("disponibleHasta").textValue())
                .setReserved(ReservedBooleanMapping.fromLabel(jn.get("reservado").textValue()));
        return hotelRoom;
    }

    public String getCode() {
        return code;
    }

    public HotelRoom setCode(String code) {
        this.code = code;
        return this;
    }

    public String getHotelName() {
        return hotelName;
    }

    public HotelRoom setHotelName(String hotelName) {
        this.hotelName = hotelName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public HotelRoom setLocation(String location) {
        this.location = location;
        return this;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public HotelRoom setRoomType(RoomType roomType) {
        this.roomType = roomType;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public HotelRoom setPrice(double price) {
        this.price = price;
        return this;
    }

    public Date getAvailableFrom() {
        return availableFrom;
    }

    public HotelRoom setAvailableFrom(String availableFrom) {
        try {
            this.availableFrom = new SimpleDateFormat("dd/MM/yyyy").parse(availableFrom);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Date getAvailableTo() {
        return availableTo;
    }

    public HotelRoom setAvailableTo(String availableTo) {
        try {
            this.availableTo = new SimpleDateFormat("dd/MM/yyyy").parse(availableTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean isReserved() {
        return reserved;
    }

    public HotelRoom setReserved(boolean reserved) {
        this.reserved = reserved;
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

    public boolean isAvailable() {
        return !isReserved();
    }

    public void reserve() {
        this.reserved = true;
    }
}
