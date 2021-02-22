package com.bootcamp.desafioquality.controller.hotelroom.dto.response;

import java.util.List;

public class HotelRoomResponseDTO {
    private String code;
    private String hotelName;
    private String location;
    private Double price;
    private List<String> availableDateRanges;

    public String getHotelName() {
        return hotelName;
    }

    public HotelRoomResponseDTO setHotelName(String hotelName) {
        this.hotelName = hotelName;
        return this;
    }

    public String getCode() {
        return code;
    }

    public HotelRoomResponseDTO setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public HotelRoomResponseDTO setLocation(String location) {
        this.location = location;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public HotelRoomResponseDTO setPrice(Double price) {
        this.price = price;
        return this;
    }

    public List<String> getAvailableDateRanges() {
        return availableDateRanges;
    }

    public HotelRoomResponseDTO setAvailableDateRanges(List<String> availableDateRanges) {
        this.availableDateRanges = availableDateRanges;
        return this;
    }
}
