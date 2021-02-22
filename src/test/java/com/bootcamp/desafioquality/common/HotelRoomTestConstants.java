package com.bootcamp.desafioquality.common;

import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.BookingDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTOBuilder;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.entity.hotel.RoomType;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.repository.util.JsonDBUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.bootcamp.desafioquality.common.PaymentMethodConstants.VALID_PAYMENT_METHOD_DTO_1;
import static com.bootcamp.desafioquality.common.PersonConstants.VALID_PERSON_DTO_1;
import static com.bootcamp.desafioquality.common.PersonConstants.VALID_PERSON_DTO_2;

public class HotelRoomTestConstants {
    // base de datos mockeada y renovada en cada test para asegurar la independencia
    public static final Supplier<List<HotelRoom>> DATABASE;
    public static final HotelRoomResponseDTO HOTEL_ROOM_RESPONSE_DTO_1 = new HotelRoomResponseDTO()
            .setCode("Code1")
            .setHotelName("Hotel1")
            .setLocation("Location1");
    public static final HotelRoomResponseDTO HOTEL_ROOM_RESPONSE_DTO_2 = new HotelRoomResponseDTO()
            .setCode("Code2")
            .setHotelName("Hotel2")
            .setLocation("Location2");

    public static final Supplier<BookingDTO> BOOKING_DTO = () -> {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setHotelCode("SE-0001");
        bookingDTO.setRoomType(RoomType.DOBLE.getLabel());
        bookingDTO.setPeopleAmount("2");
        bookingDTO.setDestination(Location.BOGOTA.getLabel());
        bookingDTO.setDateFrom("25/03/2021");
        bookingDTO.setDateTo("14/05/2021");
        PersonDTO person1 = VALID_PERSON_DTO_1.get();
        PersonDTO person2 = VALID_PERSON_DTO_2.get();
        bookingDTO.setPeople(List.of(person1, person2));
        bookingDTO.setPaymentMethod(VALID_PAYMENT_METHOD_DTO_1.get());
        return bookingDTO;
    };

    public static final Supplier<HotelRoomBookingRequestDTO> VALID_BOOKING_REQUEST = () -> {
        HotelRoomBookingRequestDTO hotelRoomBookingRequestDTO = new HotelRoomBookingRequestDTO();
        hotelRoomBookingRequestDTO.setUserName("mail1@gmail.com");
        hotelRoomBookingRequestDTO.setBooking(BOOKING_DTO.get());
        return hotelRoomBookingRequestDTO;
    };

    public static final HotelRoomBookingResponseDTO HOTEL_ROOM_BOOKING_RESPONSE_DTO_HAPPY =
            new HotelRoomBookingResponseDTO()
            .setUserName("mail1@gmail.com")
            .setBooking(BOOKING_DTO.get())
            .setStatusCode(new StatusCodeDTO().setCode(HttpStatus.OK.value()).setMessage(HotelRoomBookingResponseDTOBuilder.SUCCESS_MESSAGE))
            .setAmount(4000d)
            .setInterest(10d)
            .setTotal(4400d);

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
