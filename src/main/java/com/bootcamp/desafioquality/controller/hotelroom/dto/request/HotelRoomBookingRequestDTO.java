package com.bootcamp.desafioquality.controller.hotelroom.dto.request;

public class HotelRoomBookingRequestDTO {
    private String userName;
    private BookingDTO booking;

    public String getUserName() {
        return userName;
    }

    public HotelRoomBookingRequestDTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public HotelRoomBookingRequestDTO setBooking(BookingDTO booking) {
        this.booking = booking;
        return this;
    }
}
