package com.bootcamp.desafioquality;

import com.bootcamp.desafioquality.common.HotelRoomTestConstants;
import com.bootcamp.desafioquality.common.JsonUtil;
import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.BookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTOBuilder;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType;
import com.bootcamp.desafioquality.exception.BadRequestException;
import com.bootcamp.desafioquality.repository.hotelroom.HotelRoomRepository;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceError;
import com.bootcamp.desafioquality.service.hotelroom.impl.exception.RoomNotAvailableException;
import com.bootcamp.desafioquality.service.validation.error.FieldProcessorError;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HotelRoomIntegrationTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    HotelRoomRepository repository;

    @Autowired
    ObjectMapper objectMapper;

    private static final String QUERY_PATH = "/api/v1/hotels";
    private static final String POST_PATH = "/api/v1/hotels/booking";

    @Test
    void testQueryCases() throws Exception {
        MvcResult mvcResult = mvc.perform(get(QUERY_PATH)
                .param("dateFrom", "18/02/2021")
                .param("destination", "Buenos Aires")
                .param("destination", "Tucumán"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        List<HotelRoomResponseDTO> actualDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actualDtos).hasSize(3);
        List<String> expectedCodes = List.of("HB-0001", "BH-0002", "SH-0001");
        assertThat(actualDtos.stream().map(HotelRoomResponseDTO::getCode)).allMatch(expectedCodes::contains);

        // fecha hasta
        MvcResult mvcResult2 = mvc.perform(get(QUERY_PATH)
                .param("dateFrom", "18/02/2021")
                .param("dateTo", "25/03/2021")
                .param("destination", "Buenos Aires")
                .param("destination", "Tucumán"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        List<HotelRoomResponseDTO> actualDtos2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actualDtos2).hasSize(1);
        List<String> expectedCodes2 = List.of("BH-0002");
        assertThat(actualDtos2.stream().map(HotelRoomResponseDTO::getCode)).allMatch(expectedCodes2::contains);

        // fecha hasta muy en el futuro, no debería traer nada
        MvcResult mvcResult3 = mvc.perform(get(QUERY_PATH)
                .param("dateFrom", "18/02/2021")
                .param("dateTo", "25/03/2040")
                .param("destination", "Buenos Aires")
                .param("destination", "Tucumán"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        List<HotelRoomResponseDTO> actualDtos3 = objectMapper.readValue(mvcResult3.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actualDtos3).isEmpty();
    }

    @Test
    void testBookHotelHappy() throws Exception {
        HotelRoomBookingRequestDTO request = HotelRoomTestConstants.VALID_BOOKING_REQUEST.get();
        MvcResult mvcResult = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.JSON_GENERATOR.apply(request)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        HotelRoomBookingResponseDTO response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), HotelRoomBookingResponseDTO.class);
        StatusCodeDTO statusCode = response.getStatusCode();
        assertThat(statusCode.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(statusCode.getMessage()).isEqualTo(HotelRoomBookingResponseDTOBuilder.SUCCESS_MESSAGE);

        assertThat(response.getAmount()).isEqualTo(584000d);
        assertThat(response.getInterest()).isEqualTo(20d);
        assertThat(response.getTotal()).isEqualTo(700800d);

        // me aseguro que la reserva impacte en la BD
        BookingRequestDTO booking = request.getBooking();
        HotelRoom hotelRoom = repository.find(booking.getHotelCode()).orElseThrow();
        assertThat(hotelRoom.hasRangeAvailable(DateParser.fromString(booking.getDateFrom()), DateParser.fromString(booking.getDateTo()))).isFalse();

        // si intento hacer el post de nuevo, no va a estar disponible
        MvcResult mvcResult2 = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.JSON_GENERATOR.apply(request)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        HotelRoomBookingResponseDTO response2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), HotelRoomBookingResponseDTO.class);
        StatusCodeDTO statusCode2 = response2.getStatusCode();
        assertThat(statusCode2.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(statusCode2.getMessage()).isEqualTo(RoomNotAvailableException.MESSAGE);

        // si lo hago en otra fecha, deberia poder
        request.getBooking().setDateFrom(request.getBooking().getDateTo());
        request.getBooking().setDateTo("17/06/2021");
        MvcResult mvcResult3 = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.JSON_GENERATOR.apply(request)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        HotelRoomBookingResponseDTO response3 = objectMapper.readValue(mvcResult3.getResponse().getContentAsString(), HotelRoomBookingResponseDTO.class);
        StatusCodeDTO statusCode3 = response3.getStatusCode();
        assertThat(statusCode3.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(statusCode3.getMessage()).isEqualTo(HotelRoomBookingResponseDTOBuilder.SUCCESS_MESSAGE);
    }

    @Test
    void testBookingHotelNotFound() throws Exception {
        HotelRoomBookingRequestDTO request = HotelRoomTestConstants.VALID_BOOKING_REQUEST.get();
        String invalidCode = "not-a-code";
        request.getBooking().setHotelCode(invalidCode);
        MvcResult mvcResult = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.JSON_GENERATOR.apply(request)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(HotelRoomServiceError.HOTEL_ROOM_NOT_FOUND.getMessage());

        HotelRoomBookingRequestDTO request2 = HotelRoomTestConstants.VALID_BOOKING_REQUEST.get();
        request2.getBooking().setDestination(Location.MEDELLIN.getLabel());
        MvcResult mvcResult2 = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.JSON_GENERATOR.apply(request2)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
        assertThat(mvcResult2.getResolvedException())
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(HotelRoomServiceError.HOTEL_ROOM_NOT_FOUND.getMessage());
    }

    @Test void testBadRequestCases() throws Exception {
        HotelRoomBookingRequestDTO request = HotelRoomTestConstants.VALID_BOOKING_REQUEST.get();
        request.setUserName("not-an-email");
        MvcResult mvcResult = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.JSON_GENERATOR.apply(request)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(FieldProcessorError.INVALID_MAIL_FORMAT.getMessage());
        HotelRoomBookingRequestDTO request2 = HotelRoomTestConstants.VALID_BOOKING_REQUEST.get();
        request2.getBooking().getPaymentMethod().setType("DEBIT").setDues(5);
        MvcResult mvcResult2 = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.JSON_GENERATOR.apply(request2)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
        assertThat(mvcResult2.getResolvedException())
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(PaymentMethodType.PaymentMethodTypeError.INSTALLMENTS_NOT_ALLOWED.getMsg());
    }
}
