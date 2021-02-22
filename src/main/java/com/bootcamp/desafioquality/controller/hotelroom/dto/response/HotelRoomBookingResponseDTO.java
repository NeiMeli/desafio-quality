package com.bootcamp.desafioquality.controller.hotelroom.dto.response;

import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HotelRoomBookingResponseDTO {
    private String userName;
    private Double amount;
    private Double interest;
    private Double total;
    private BookingResponseDTO booking;
    private StatusCodeDTO statusCode;

    public String getUserName() {
        return userName;
    }

    public HotelRoomBookingResponseDTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public HotelRoomBookingResponseDTO setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public Double getInterest() {
        return interest;
    }

    public HotelRoomBookingResponseDTO setInterest(double interest) {
        this.interest = interest;
        return this;
    }

    public Double getTotal() {
        return total;
    }

    public HotelRoomBookingResponseDTO setTotal(double total) {
        this.total = total;
        return this;
    }

    public BookingResponseDTO getBooking() {
        return booking;
    }

    public HotelRoomBookingResponseDTO setBooking(BookingResponseDTO booking) {
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
