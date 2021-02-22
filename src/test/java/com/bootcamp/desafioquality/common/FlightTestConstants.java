package com.bootcamp.desafioquality.common;

import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.flight.dto.FlightReservationDTO;
import com.bootcamp.desafioquality.controller.flight.dto.request.FlightReservationRequestDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTOBuilder;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightResponseDTO;
import com.bootcamp.desafioquality.entity.flight.Flight;
import com.bootcamp.desafioquality.repository.util.JsonDBUtil;
import com.bootcamp.desafioquality.service.flight.exception.FlightServiceError;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.bootcamp.desafioquality.common.PersonConstants.VALID_PERSON_DTO_1;
import static com.bootcamp.desafioquality.common.PersonConstants.VALID_PERSON_DTO_2;

public class FlightTestConstants {
    public static final Supplier<List<Flight>> DATABASE;
    public static final Supplier<FlightReservationDTO> VALID_RESERVATION_DTO_1 = () -> {
        FlightReservationDTO reservation = new FlightReservationDTO();
        reservation.setPeople(List.of(VALID_PERSON_DTO_1.get(), VALID_PERSON_DTO_2.get()))
                .setFlightNumber("BATU-5536")
                .setSeats("2")
                .setFlightNumber("BATU-5536")
                .setDateFrom("10/02/2021")
                .setDateTo("17/02/2021")
                .setOrigin("Buenos Aires")
                .setDestination("Tucumán")
                .setSeatType("Economy");
        reservation.setPaymentMethod(PaymentMethodConstants.VALID_PAYMENT_METHOD_DTO_2.get());
        return reservation;
    };
    public static final Supplier<FlightReservationRequestDTO> VALID_RESERVATION_REQUEST = () -> {
        FlightReservationRequestDTO reservationRequest = new FlightReservationRequestDTO();
        reservationRequest.setUserName("mail1@gmail.com");
        reservationRequest.setFlightReservation(VALID_RESERVATION_DTO_1.get());
        return reservationRequest;
    };

    public static final FlightResponseDTO FLIGHT_RESPONSE_DTO_1 = new FlightResponseDTO()
            .setCode("CODE1")
            .setDateFrom("01/01/2021")
            .setDateTo("08/01/2021")
            .setOrigin("Orig1")
            .setDestination("Dest1")
            .setPrice(2000d)
            .setSeatType("Economy");
    public static final FlightResponseDTO FLIGHT_RESPONSE_DTO_2 = new FlightResponseDTO()
            .setCode("CODE2")
            .setDateFrom("18/01/2021")
            .setDateTo("28/01/2021")
            .setOrigin("Orig2")
            .setDestination("Dest2")
            .setPrice(4400d)
            .setSeatType("Business");

    public static final FlightReservationResponseDTO FLIGHT_RESERVATION_RESPONSE_DTO_HAPPY = new FlightReservationResponseDTO()
            .setFlightReservation(VALID_RESERVATION_DTO_1.get())
            .setUserName("mail1@gmail.com")
            .setAmount(1000d)
            .setTotal(1200d)
            .setInterest(20d)
            .setStatusCode(new StatusCodeDTO().setCode(HttpStatus.OK.value()).setMessage(FlightReservationResponseDTOBuilder.SUCCESS_MESSAGE));
    public static final FlightReservationResponseDTO FLIGHT_RESERVATION_RESPONSE_DTO_NOT_FOUND = new FlightReservationResponseDTO()
            .setFlightReservation(VALID_RESERVATION_DTO_1.get())
            .setUserName("mail1@gmail.com")
            .setStatusCode(new StatusCodeDTO().setCode(HttpStatus.NOT_FOUND.value()).setMessage(FlightServiceError.FLIGHT_NOT_FOUND.getMessage()));

    static {
        DATABASE = () -> {
            final List<Flight> hotelRoomList = new ArrayList<>();
            JsonNode jsonNodes = null;
            try {
                jsonNodes = JsonDBUtil.parseDatabase("src/main/resources/database/json/flights.json");
            } catch (Exception e) {
                // ignore);
            }
            if (jsonNodes != null) {
                for (JsonNode jsonNode : jsonNodes) {
                    hotelRoomList.add(Flight.fromJson(jsonNode));
                }
            }
            return hotelRoomList;
        };
    }

    public static final String JSON_REQUEST = "{\n" +
            "    \"flightReservation\": {\n" +
            "        \"seatType\": \"Economy\",\n" +
            "        \"origin\": \"Buenos Aires\",\n" +
            "        \"dateTo\": \"17\\/02\\/2021\",\n" +
            "        \"destination\": \"Tucumán\",\n" +
            "        \"paymentMethod\": {\n" +
            "            \"dues\": 3,\n" +
            "            \"number\": \"1234-1234\",\n" +
            "            \"type\": \"CREDIT\"\n" +
            "        },\n" +
            "        \"dateFrom\": \"10\\/02\\/2021\",\n" +
            "        \"seats\": \"2\",\n" +
            "        \"people\": [\n" +
            "            {\n" +
            "                \"lastName\": \"lastName1\",\n" +
            "                \"mail\": \"mail1@gmail.com\",\n" +
            "                \"name\": \"name1\",\n" +
            "                \"birthDate\": \"10\\/05\\/1991\",\n" +
            "                \"dni\": \"123456\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"lastName\": \"lastName2\",\n" +
            "                \"mail\": \"mail2@gmail.com\",\n" +
            "                \"name\": \"name2\",\n" +
            "                \"birthDate\": \"14\\/11\\/1978\",\n" +
            "                \"dni\": \"654321\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"flightNumber\": \"BATU-5536\"\n" +
            "    },\n" +
            "    \"userName\": \"mail1@gmail.com\"\n" +
            "}";
}
