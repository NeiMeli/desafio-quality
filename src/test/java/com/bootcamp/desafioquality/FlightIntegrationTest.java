package com.bootcamp.desafioquality;

import com.bootcamp.desafioquality.common.JsonUtil;
import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.flight.dto.request.FlightReservationRequestDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTOBuilder;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightResponseDTO;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType;
import com.bootcamp.desafioquality.service.flight.exception.FlightServiceError;
import com.bootcamp.desafioquality.service.flight.exception.FlightServiceException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.bootcamp.desafioquality.common.FlightTestConstants.JSON_REQUEST;
import static com.bootcamp.desafioquality.common.FlightTestConstants.VALID_RESERVATION_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FlightIntegrationTest {
    private static final String QUERY_PATH = "/api/v1/flights";
    private static final String POST_PATH = "/api/v1/flights/flight-reservation";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testQueryCases() throws Exception {
        MvcResult mvcResult = mvc.perform(get(QUERY_PATH)
                .param("dateFrom", "15/02/2021")
                .param("dateTo", "28/02/2021")
                .param("origin", "Bogotá")
                .param("destination", "Buenos Aires")
                .param("seatType", "Business"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        List<FlightResponseDTO> actualDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actualDtos).hasSize(1);
        assertThat(actualDtos.get(0).getCode()).isEqualTo("BOBA-6567");

        // esta query no deberia traerme nada
        MvcResult mvcResult2 = mvc.perform(get(QUERY_PATH)
                .param("dateFrom", "15/02/2016")
                .param("dateTo", "28/02/2021")
                .param("origin", "Bogotá")
                .param("destination", "Buenos Aires")
                .param("seatType", "Business"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        List<FlightResponseDTO> actualDtos2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actualDtos2).hasSize(0);

        // listall
        MvcResult mvcResult3 = mvc.perform(get(QUERY_PATH))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        List<FlightResponseDTO> actualDtos3 = objectMapper.readValue(mvcResult3.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actualDtos3).hasSize(12);
    }

    @Test
    void testReserveHappy() throws Exception {
        MvcResult mvcResult = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSON_REQUEST))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        FlightReservationResponseDTO response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), FlightReservationResponseDTO.class);
        StatusCodeDTO statusCode = response.getStatusCode();
        assertThat(statusCode.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(statusCode.getMessage()).isEqualTo(FlightReservationResponseDTOBuilder.SUCCESS_MESSAGE);

        assertThat(response.getAmount()).isEqualTo(14640d);
        assertThat(response.getInterest()).isEqualTo(5d);
        assertThat(response.getTotal()).isEqualTo(15372d);

        // no encuentra el vuelo
        FlightReservationRequestDTO flightReservationRequestDTO = VALID_RESERVATION_REQUEST.get();
        flightReservationRequestDTO.getFlightReservation().setDateFrom("15/02/2016");
        MvcResult mvcResult2 = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.JSON_GENERATOR.apply(flightReservationRequestDTO)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        FlightReservationResponseDTO response2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), FlightReservationResponseDTO.class);
        StatusCodeDTO statusCode2 = response2.getStatusCode();
        assertThat(statusCode2.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(statusCode2.getMessage()).isEqualTo(FlightServiceError.FLIGHT_NOT_FOUND.getMessage());

        assertThat(response2.getAmount()).isNull();
        assertThat(response2.getInterest()).isNull();
        assertThat(response2.getTotal()).isNull();
    }

    @Test
    void testReserveBadRequest() throws Exception {
        FlightReservationRequestDTO flightReservationRequestDTO = VALID_RESERVATION_REQUEST.get();
        String invalidDate = "1502/2016";
        flightReservationRequestDTO.getFlightReservation().setDateFrom(invalidDate);
        MvcResult mvcResult = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.JSON_GENERATOR.apply(flightReservationRequestDTO)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(FlightServiceException.class)
                .hasMessageContaining(String.format(DateParser.ERROR_MESSAGE, invalidDate));
        FlightReservationRequestDTO flightReservationRequestDTO2 = VALID_RESERVATION_REQUEST.get();
        String invalidPmType = "not-a-payment-method-type";
        flightReservationRequestDTO2.getFlightReservation().getPaymentMethod().setType(invalidPmType);
        flightReservationRequestDTO.getFlightReservation().setDateFrom(invalidDate);
        MvcResult mvcResult2 = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.JSON_GENERATOR.apply(flightReservationRequestDTO2)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
        assertThat(mvcResult2.getResolvedException())
                .isInstanceOf(FlightServiceException.class)
                .hasMessageContaining(PaymentMethodType.PaymentMethodTypeError.PAYMENT_METHOD_TYPE_NOT_FOUND.getMsg(invalidPmType));
    }
}
