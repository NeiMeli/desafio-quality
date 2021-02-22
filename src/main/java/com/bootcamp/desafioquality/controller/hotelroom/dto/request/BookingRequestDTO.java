package com.bootcamp.desafioquality.controller.hotelroom.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.bootcamp.desafioquality.controller.dtoutil.Message.REQUIRED_FIELD;

public class BookingRequestDTO {
    @NotEmpty(message = REQUIRED_FIELD) private String dateFrom;
    @NotEmpty(message = REQUIRED_FIELD) private String dateTo;
    @NotEmpty(message = REQUIRED_FIELD) private String destination;
    @NotEmpty(message = REQUIRED_FIELD) private String hotelCode;
    @NotEmpty(message = REQUIRED_FIELD) private String peopleAmount;
    @NotEmpty(message = REQUIRED_FIELD) private String roomType;
    @NotEmpty(message = REQUIRED_FIELD) private List<PersonDTO> people;
    @NotNull(message = REQUIRED_FIELD) private PaymentMethodDTO paymentMethod;

    public String getDateFrom() {
        return dateFrom;
    }

    public BookingRequestDTO setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public String getDateTo() {
        return dateTo;
    }

    public BookingRequestDTO setDateTo(String dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public BookingRequestDTO setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public BookingRequestDTO setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
        return this;
    }

    public String getPeopleAmount() {
        return peopleAmount;
    }

    public BookingRequestDTO setPeopleAmount(String peopleAmount) {
        this.peopleAmount = peopleAmount;
        return this;
    }

    public String getRoomType() {
        return roomType;
    }

    public BookingRequestDTO setRoomType(String roomType) {
        this.roomType = roomType;
        return this;
    }

    public List<PersonDTO> getPeople() {
        return people;
    }

    public BookingRequestDTO setPeople(List<PersonDTO> people) {
        this.people = people;
        return this;
    }

    public PaymentMethodDTO getPaymentMethod() {
        return paymentMethod;
    }

    public BookingRequestDTO setPaymentMethod(PaymentMethodDTO paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }
}
