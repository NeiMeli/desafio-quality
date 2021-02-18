package com.bootcamp.desafioquality.controller.hotelroom.dto.request;

import java.util.List;

public class BookingDTO {
    private String dateFrom;
    private String dateTo;
    private String destination;
    private String hotelCode;
    private Integer peopleAmount;
    private String roomType;
    private List<PersonDTO> people;
    private PaymentMethodDTO paymentMethod;

    public String getDateFrom() {
        return dateFrom;
    }

    public BookingDTO setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public String getDateTo() {
        return dateTo;
    }

    public BookingDTO setDateTo(String dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public BookingDTO setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public BookingDTO setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
        return this;
    }

    public Integer getPeopleAmount() {
        return peopleAmount;
    }

    public BookingDTO setPeopleAmount(Integer peopleAmount) {
        this.peopleAmount = peopleAmount;
        return this;
    }

    public String getRoomType() {
        return roomType;
    }

    public BookingDTO setRoomType(String roomType) {
        this.roomType = roomType;
        return this;
    }

    public List<PersonDTO> getPeople() {
        return people;
    }

    public BookingDTO setPeople(List<PersonDTO> people) {
        this.people = people;
        return this;
    }

    public PaymentMethodDTO getPaymentMethod() {
        return paymentMethod;
    }

    public BookingDTO setPaymentMethod(PaymentMethodDTO paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }
}
