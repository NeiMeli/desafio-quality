package com.bootcamp.desafioquality.controller.hotelroom.dto.response;

import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.BookingDTO;

public class HotelRoomBookingResponseDTO {
    private String userName;
    private double amount;
    private double interest;
    private double total;
    private BookingDTO booking;
    private StatusCodeDTO statusCode;

    public String getUserName() {
        return userName;
    }

    public HotelRoomBookingResponseDTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public HotelRoomBookingResponseDTO setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public double getInterest() {
        return interest;
    }

    public HotelRoomBookingResponseDTO setInterest(double interest) {
        this.interest = interest;
        return this;
    }

    public double getTotal() {
        return total;
    }

    public HotelRoomBookingResponseDTO setTotal(double total) {
        this.total = total;
        return this;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public HotelRoomBookingResponseDTO setBooking(BookingDTO booking) {
        this.booking = booking;
        return this;
    }

    public StatusCodeDTO getStatusCode() {
        return statusCode;
    }

    public HotelRoomBookingResponseDTO setStatusCode(StatusCodeDTO statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}
