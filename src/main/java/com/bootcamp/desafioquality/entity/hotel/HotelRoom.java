package com.bootcamp.desafioquality.entity.hotel;

import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.date.DateRange;
import com.bootcamp.desafioquality.entity.Persistable;
import com.bootcamp.desafioquality.entity.location.Location;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotelRoom implements Persistable<String> {
    private String code;
    private String hotelName;
    private Location location;
    private RoomType roomType;
    private double price;
    private Date availableFrom;
    private Date availableTo;
    private HotelRoomSchedule schedule;

    public static HotelRoom fromJson(JsonNode jn) {
        HotelRoom hotelRoom = new HotelRoom();
        hotelRoom.setCode(jn.get("codigoHotel").textValue())
                .setHotelName(jn.get("nombre").textValue())
                .setLocation(Location.fromLabel(jn.get("lugar/Ciudad").textValue()))
                .setRoomType(RoomType.fromLabel(jn.get("tipoDeHabitacion").textValue()))
                .setPrice(jn.get("precioPorNoche").doubleValue());
        Date avlFrom = DateParser.fromString(jn.get("disponibleDesde").textValue());
        Date avlTo = DateParser.fromString(jn.get("disponibleHasta").textValue());
        hotelRoom.schedule = new HotelRoomSchedule(avlFrom, avlTo);
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

    public Location getLocation() {
        return location;
    }

    public HotelRoom setLocation(Location location) {
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

    public boolean hasRangeAvailable(@NotNull Date dateFrom, @NotNull Date dateTo) {
        return schedule.isRangeAvailable(dateFrom, dateTo);
    }

    public boolean hasDateAvailable(@NotNull Date dateFrom) {
        return schedule.isDateAvailable(dateFrom);
    }

    public boolean hasDatesAvailable() {
        return schedule.hasDatesAvailable();
    }

    public void reserve(@NotNull Date dateFrom, @NotNull Date dateTo) {
        schedule.reserveRange(dateFrom, dateTo);
    }

    public DateRange getNextAvailableRange() {
        return schedule.getNextAvailableRange();
    }
}
