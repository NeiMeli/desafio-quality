package com.bootcamp.desafioquality.controller.hotelroom.dto.response;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponseDTO {
     private String dateFrom;
     private String dateTo;
     private String destination;
     private String hotelCode;
     private String peopleAmount;
     private String roomType;
     private List<PersonDTO> people;

    public String getDateFrom() {
        return dateFrom;
    }

    public BookingResponseDTO setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public String getDateTo() {
        return dateTo;
    }

    public BookingResponseDTO setDateTo(String dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public BookingResponseDTO setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public BookingResponseDTO setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
        return this;
    }

    public String getPeopleAmount() {
        return peopleAmount;
    }

    public BookingResponseDTO setPeopleAmount(String peopleAmount) {
        this.peopleAmount = peopleAmount;
        return this;
    }

    public String getRoomType() {
        return roomType;
    }

    public BookingResponseDTO setRoomType(String roomType) {
        this.roomType = roomType;
        return this;
    }

    public List<PersonDTO> getPeople() {
        return people;
    }

    public BookingResponseDTO setPeople(List<PersonDTO> people) {
        this.people = people;
        return this;
    }
}
