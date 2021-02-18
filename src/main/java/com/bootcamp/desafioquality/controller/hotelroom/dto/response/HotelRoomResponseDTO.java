package com.bootcamp.desafioquality.controller.hotelroom.dto.response;

public class HotelRoomResponseDTO {
    private String code;
    private String hotelName;
    private String location;

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
}
