package com.bootcamp.desafioquality.common;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.BookingDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PaymentMethodDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.entity.hotel.RoomType;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType;
import com.bootcamp.desafioquality.repository.util.JsonDBUtil;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class HotelRoomTestConstants {
    // base de datos mockeada y renovada en cada test para asegurar la independencia
    public static final Supplier<List<HotelRoom>> DATABASE;
    public static final Supplier<PersonDTO> VALID_PERSON_DTO_1 = () -> new PersonDTO()
            .setName("name1")
            .setLastName("lastName1")
            .setMail("mail1@gmail.com")
            .setDni("123456")
            .setBirthDate("10/05/1991");
    public static final Supplier<HotelRoomBookingRequestDTO> VALID_BOOKING_REQUEST = () -> {
        HotelRoomBookingRequestDTO hotelRoomBookingRequestDTO = new HotelRoomBookingRequestDTO();
        hotelRoomBookingRequestDTO.setUserName("mail1@gmail.com");
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setHotelCode("SE-0001");
        bookingDTO.setRoomType(RoomType.DOBLE.getLabel());
        bookingDTO.setPeopleAmount("2");
        bookingDTO.setDestination(Location.BOGOTA.getLabel());
        bookingDTO.setDateFrom("25/03/2021");
        bookingDTO.setDateTo("14/05/2021");
        PersonDTO person1 = VALID_PERSON_DTO_1.get();
        PersonDTO person2 = new PersonDTO()
                .setName("name2")
                .setLastName("lastName2")
                .setMail("mail2@gmail.com")
                .setDni("654321")
                .setBirthDate("14/11/1978");
        bookingDTO.setPeople(List.of(person1, person2));
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setType(PaymentMethodType.CREDIT.getLabel());
        paymentMethodDTO.setNumber("1234-1234");
        paymentMethodDTO.setDues(9);
        bookingDTO.setPaymentMethod(paymentMethodDTO);
        hotelRoomBookingRequestDTO.setBooking(bookingDTO);
        return hotelRoomBookingRequestDTO;
    };


    static {
        DATABASE = () -> {
            final List<HotelRoom> hotelRoomList = new ArrayList<>();
            JsonNode jsonNodes = null;
            try {
                jsonNodes = JsonDBUtil.parseDatabase("src/main/resources/database/json/hotel-rooms.json");
            } catch (Exception e) {
                // ignore);
            }
            if (jsonNodes != null) {
                for (JsonNode jsonNode : jsonNodes) {
                    hotelRoomList.add(HotelRoom.fromJson(jsonNode));
                }
            }
            return hotelRoomList;
        };
    }
}
