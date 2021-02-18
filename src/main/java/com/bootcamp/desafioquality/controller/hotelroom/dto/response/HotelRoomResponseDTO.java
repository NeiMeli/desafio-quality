package com.bootcamp.desafioquality.controller.hotelroom.dto.response;

public class HotelRoomResponseDTO {
    private String hotelName;
    private String code;

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
}
