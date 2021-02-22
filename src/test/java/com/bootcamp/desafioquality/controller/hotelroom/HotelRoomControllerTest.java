package com.bootcamp.desafioquality.controller.hotelroom;

import com.bootcamp.desafioquality.common.JsonUtil;
import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTOBuilder;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.exception.BadRequestException;
import com.bootcamp.desafioquality.service.hotelroom.HotelRoomService;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceError;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceException;
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

import java.util.List;
import java.util.stream.Collectors;

import static com.bootcamp.desafioquality.common.BadRequestAsserter.assertBadRequest;
import static com.bootcamp.desafioquality.common.HotelRoomTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HotelRoomControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    HotelRoomService service;

    @Autowired
    ObjectMapper objectMapper;

    private static final String QUERY_PATH = "/api/v1/hotels";
    private static final String POST_PATH = "/api/v1/hotels/booking";

    @Test
    void testQueryHappy() throws Exception {
        List<HotelRoomResponseDTO> expectedDtos = List.of(HOTEL_ROOM_RESPONSE_DTO_1, HOTEL_ROOM_RESPONSE_DTO_2);
        when(service.query(any())).thenReturn(expectedDtos);
        MvcResult mvcResult = mvc.perform(get(QUERY_PATH)
                .param("destination", "Buenos Aires")
                .param("dateTo", "18/09/2021"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        List<HotelRoomResponseDTO> actualDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actualDtos).hasSize(2);
        List<String> codes = expectedDtos.stream().map(HotelRoomResponseDTO::getCode).collect(Collectors.toList());
        assertThat(actualDtos.stream().map(HotelRoomResponseDTO::getCode)).allMatch(codes::contains);
    }

    @Test
    void testQueryBadRequests() throws Exception {
        final String invalidDate = "2208/2020";
        assertBadRequest(mvc.perform(get(QUERY_PATH).param("dateFrom", invalidDate)), String.format(DateParser.ERROR_MESSAGE, invalidDate));
        assertBadRequest(mvc.perform(get(QUERY_PATH).param("dateTo", invalidDate)), String.format(DateParser.ERROR_MESSAGE, invalidDate));
        final String invalidLocation = "not-a-location";
        assertBadRequest(mvc.perform(get(QUERY_PATH).param("destination", invalidLocation)), String.format(Location.LocationNotFoundException.MESSAGE, invalidLocation));
    }

    @Test
    void testBookingHappy() throws Exception {
        when(service.bookHotelRoom(any()))
                .thenReturn(HOTEL_ROOM_BOOKING_RESPONSE_DTO_HAPPY);
        String json = JsonUtil.JSON_GENERATOR.apply(VALID_BOOKING_REQUEST.get());
        MvcResult mvcResult = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        HotelRoomBookingResponseDTO actualDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), HotelRoomBookingResponseDTO.class);
        StatusCodeDTO statusCode = actualDto.getStatusCode();
        assertThat(statusCode.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(statusCode.getMessage()).isEqualTo(HotelRoomBookingResponseDTOBuilder.SUCCESS_MESSAGE);

        assertThat(actualDto.getAmount()).isEqualTo(HOTEL_ROOM_BOOKING_RESPONSE_DTO_HAPPY.getAmount());
        assertThat(actualDto.getInterest()).isEqualTo(HOTEL_ROOM_BOOKING_RESPONSE_DTO_HAPPY.getInterest());
        assertThat(actualDto.getTotal()).isEqualTo(HOTEL_ROOM_BOOKING_RESPONSE_DTO_HAPPY.getTotal());
    }

    @Test
    void testBookingBadRequests() throws Exception {
        when(service.bookHotelRoom(any()))
                .thenThrow(new HotelRoomServiceException(HotelRoomServiceError.HOTEL_ROOM_NOT_FOUND.getMessage()));
        String json = JsonUtil.JSON_GENERATOR.apply(VALID_BOOKING_REQUEST.get());
        MvcResult mvcResult = mvc.perform(post(POST_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(HotelRoomServiceError.HOTEL_ROOM_NOT_FOUND.getMessage());
    }
}