package com.bootcamp.desafioquality.controller.flight;

import com.bootcamp.desafioquality.common.JsonUtil;
import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.flight.dto.request.FlightReservationRequestDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTOBuilder;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightResponseDTO;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.entity.flight.SeatType;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.exception.BadRequestException;
import com.bootcamp.desafioquality.service.flight.FlightService;
import com.bootcamp.desafioquality.service.flight.exception.FlightServiceError;
import com.bootcamp.desafioquality.service.flight.exception.FlightServiceException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

import static com.bootcamp.desafioquality.common.BadRequestAsserter.assertBadRequest;
import static com.bootcamp.desafioquality.common.FlightTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FlightControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    FlightService service;

    @Autowired
    ObjectMapper objectMapper;

    private static final String QUERY_PATH = "/api/v1/flights";
    private static final String POST_PATH = "/api/v1/flights/flight-reservation";

    @Test
    void testQueryHappy() throws Exception {
        List<FlightResponseDTO> expectedDtos = List.of(FLIGHT_RESPONSE_DTO_1, FLIGHT_RESPONSE_DTO_2);
        when(service.query(any()))
                .thenReturn(expectedDtos);
        MvcResult mvcResult = mvc.perform(get(QUERY_PATH)
                .param("dateFrom", "22/08/2020")
                .param("destination", "Buenos Aires"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        List<FlightResponseDTO> actualDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actualDtos).hasSize(2);
        List<String> codes = expectedDtos.stream().map(FlightResponseDTO::getCode).collect(Collectors.toList());
        assertThat(actualDtos.stream().map(FlightResponseDTO::getCode)).allMatch(codes::contains);
    }

    @Test
    void testQueryBadRequests() throws Exception {
        final String invalidDate = "2208/2020";
        assertBadRequest(mvc.perform(get(QUERY_PATH).param("dateFrom", invalidDate)), String.format(DateParser.ERROR_MESSAGE, invalidDate));
        assertBadRequest(mvc.perform(get(QUERY_PATH).param("dateTo", invalidDate)), String.format(DateParser.ERROR_MESSAGE, invalidDate));
        final String invalidLocation = "not-a-location";
        assertBadRequest(mvc.perform(get(QUERY_PATH).param("origin", invalidLocation)), String.format(Location.LocationNotFoundException.MESSAGE, invalidLocation));
        assertBadRequest(mvc.perform(get(QUERY_PATH).param("destination", invalidLocation)), String.format(Location.LocationNotFoundException.MESSAGE, invalidLocation));
        final String invalidSeatType = "not-a-seatType";
        assertBadRequest(mvc.perform(get(QUERY_PATH).param("seatType", invalidSeatType)), String.format(SeatType.SeatTypeNotFoundException.MESSAGE, invalidSeatType));
    }


    @Test
    void testReserveHappy() throws Exception {
        when(service.reserveFlight(any()))
                .thenReturn(FLIGHT_RESERVATION_RESPONSE_DTO_HAPPY);
        MvcResult mvcResult = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSON_REQUEST))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        FlightReservationResponseDTO actualDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), FlightReservationResponseDTO.class);
        StatusCodeDTO statusCode = actualDto.getStatusCode();
        assertThat(statusCode.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(statusCode.getMessage()).isEqualTo(FlightReservationResponseDTOBuilder.SUCCESS_MESSAGE);

        assertThat(actualDto.getAmount()).isEqualTo(FLIGHT_RESERVATION_RESPONSE_DTO_HAPPY.getAmount());
        assertThat(actualDto.getInterest()).isEqualTo(FLIGHT_RESERVATION_RESPONSE_DTO_HAPPY.getInterest());
        assertThat(actualDto.getTotal()).isEqualTo(FLIGHT_RESERVATION_RESPONSE_DTO_HAPPY.getTotal());
    }

    @Test
    void testReserveBadRequest() throws Exception {
        when(service.reserveFlight(any()))
                .thenThrow(new FlightServiceException(FlightServiceError.FLIGHT_NOT_FOUND.getMessage()));
        MvcResult mvcResult = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSON_REQUEST))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(FlightServiceError.FLIGHT_NOT_FOUND.getMessage());
    }

    @Test
    void testInvalidJson() throws Exception {
        FlightReservationRequestDTO reservationRequest = VALID_RESERVATION_REQUEST.get();
        reservationRequest.setUserName(null);
        assertEmptyFieldError(reservationRequest, "userName");

        reservationRequest.setUserName("user@gmail.com");
        reservationRequest.setFlightReservation(null);
        assertEmptyFieldError(reservationRequest, "flightReservation");

        FlightReservationRequestDTO reservationRequest2 = VALID_RESERVATION_REQUEST.get();
        reservationRequest2.getFlightReservation().setPaymentMethod(null);
        assertEmptyFieldError(reservationRequest2, "paymentMethod");

        FlightReservationRequestDTO reservationRequest3 = VALID_RESERVATION_REQUEST.get();
        reservationRequest3.getFlightReservation().getPaymentMethod().setType(null);
        assertEmptyFieldError(reservationRequest3, "type");
    }

    private void assertEmptyFieldError(FlightReservationRequestDTO request, String fieldName) throws Exception {
        String json = JsonUtil.JSON_GENERATOR.apply(request);
        MvcResult mvcResult = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(MethodArgumentNotValidException.class)
                .hasMessageContaining(fieldName);
    }
}