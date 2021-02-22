package com.bootcamp.desafioquality.controller.hotelroom.dto.request;

import javax.validation.constraints.NotEmpty;

import static com.bootcamp.desafioquality.controller.dtoutil.Message.REQUIRED_FIELD;

public class HotelRoomBookingRequestDTO {
    @NotEmpty(message = REQUIRED_FIELD) private String userName;
    private BookingRequestDTO booking;


    public String getUserName() {
        return userName;
    }

    public HotelRoomBookingRequestDTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public BookingRequestDTO getBooking() {
        return booking;
    }

    public HotelRoomBookingRequestDTO setBooking(BookingRequestDTO booking) {
        this.booking = booking;
        return this;
    }
}
