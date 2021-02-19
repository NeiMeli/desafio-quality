package com.bootcamp.desafioquality.service.hotelroom.impl;

import com.bootcamp.desafioquality.common.CacheDBTableMock;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.BookingDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PaymentMethodDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.date.DateRangeValidator;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.entity.hotel.RoomType;
import com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType;
import com.bootcamp.desafioquality.repository.hotelroom.impl.HotelRoomCacheRespository;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceException;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQuery;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQueryException;
import com.bootcamp.desafioquality.service.validation.ServiceValidationError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bootcamp.desafioquality.common.HotelRoomTestConstants.DATABASE;
import static com.bootcamp.desafioquality.date.DateParser.ERROR_MESSAGE;
import static com.bootcamp.desafioquality.entity.location.Location.BOGOTA;
import static com.bootcamp.desafioquality.entity.location.Location.LocationNotFoundException;
import static com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceError.*;
import static com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQueryException.HotelRoomQueryExceptionMessage;
import static com.bootcamp.desafioquality.service.validation.ServiceValidationError.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class HotelRoomServiceImplTest {
    @MockBean
    HotelRoomCacheRespository repository;

    @Autowired
    HotelRoomServiceImpl service;

    @Test
    void testListAllAvailable() {
        List<HotelRoom> hotelRoomList = DATABASE.get();
        when(repository.getDatabase())
                .thenReturn(new CacheDBTableMock<>(hotelRoomList));
        when(repository.listWhere(any())).thenCallRealMethod();

        HotelRoomQuery hotelRoomQuery = new HotelRoomQuery();
        int totalSize = hotelRoomList.size(); // primero están todas disponibles
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS).hasSize(totalSize);

        // reservo 5
        List<String> reservedRooms = new ArrayList<>();
        IntStream.range(0, 4).forEach(index -> {
            HotelRoom hotelRoom = hotelRoomList.get(index);
            hotelRoom.reserve();
            reservedRooms.add(hotelRoom.getCode());
        });

        int availableSizeAfterReserve = totalSize - reservedRooms.size();
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS2 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS2).hasSize(availableSizeAfterReserve);

        // me aseguro de que sólo me devuelva las disponibles
        assertThat(hotelRoomResponseDTOS2).noneMatch(dto -> reservedRooms.contains(dto.getCode()));
    }

    @Test
    void testQueryCases() {
        List<HotelRoom> hotelRoomList = DATABASE.get();
        when(repository.getDatabase())
                .thenReturn(new CacheDBTableMock<>(hotelRoomList));
        when(repository.listWhere(any())).thenCallRealMethod();

        HotelRoomQuery hotelRoomQuery = new HotelRoomQuery();

        // un destino
        final String bsas = "Buenos Aires";
        final String cartagena = "Cartagena";
        hotelRoomQuery.withDestinations(bsas);
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS2 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS2).hasSize(2);
        assertThat(hotelRoomResponseDTOS2).allMatch(dto -> dto.getLocation().equals(bsas));

        // dos destinos
        hotelRoomQuery.withDestinations(bsas, cartagena);
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS3 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS3).hasSize(3);
        assertThat(hotelRoomResponseDTOS2).allMatch(dto -> dto.getLocation().equals(bsas) || dto.getLocation().equals(cartagena));

        // si los reservo, no tienen que aparecer
        List<String> codes = hotelRoomResponseDTOS3.stream().map(HotelRoomResponseDTO::getCode).collect(Collectors.toList());
        hotelRoomList.stream().filter(hr -> codes.contains(hr.getCode()))
                .forEach(HotelRoom::reserve);
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS4 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS4).isEmpty();

        // fecha desde
        hotelRoomQuery.withoutDestinations();
        hotelRoomQuery.withDateFrom("12/02/2021");
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS5 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS5).hasSize(6);

        // fecha desde y hasta
        hotelRoomQuery.withDateTo("27/03/2021");
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS6 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS6).hasSize(2);

        // todos los de hotelRoomResponseDTOS6 tienen que cumplir tambien el desde.
        assertThat(hotelRoomResponseDTOS6.stream().map(HotelRoomResponseDTO::getCode))
                .allMatch(code -> hotelRoomResponseDTOS5.stream().map(HotelRoomResponseDTO::getCode).collect(Collectors.toList()).contains(code));

        // solo hasta
        hotelRoomQuery.withoutDateFrom();
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS7 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS7).hasSize(4);

        // agrego desde y tambien ubicacion
        final String bogota = "Bogotá";
        hotelRoomQuery.withDateFrom("23/01/2021");
        hotelRoomQuery.withDestinations(bogota);
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS8 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS8).hasSize(2);
        assertThat(hotelRoomResponseDTOS8).allMatch(dto -> dto.getLocation().equals(bogota));

        // con un hasta muy en el futuro, no debería traerme nada
        hotelRoomQuery.withDateTo("17/08/2040");
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS9 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS9).isEmpty();
        hotelRoomQuery.withoutDateTo();

        // con un desde muy en el futuro, no debería traerme nada
        hotelRoomQuery.withDateFrom("17/08/2040");
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS10 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS10).isEmpty();
    }

    @Test
    void testQueryBadRequests() {
        HotelRoomQuery hotelRoomQuery = new HotelRoomQuery();
        // ubicacion invalida
        String invalidLocation = "non-existent-destination";
        assertThatExceptionOfType(LocationNotFoundException.class)
                .isThrownBy(() -> hotelRoomQuery.withDestinations(invalidLocation))
                .withMessageContaining(LocationNotFoundException.MESSAGE, invalidLocation);

        // fecha hasta invalida
        hotelRoomQuery.withDateFrom("12/02/2021");
        String invalidDateTo = "11/02/2021";
        assertThatExceptionOfType(HotelRoomQueryException.class)
                .isThrownBy(() -> hotelRoomQuery.withDateTo(invalidDateTo))
                .withMessageContaining(HotelRoomQueryExceptionMessage.INVALID_DATE_TO.getMessage());

        // fecha desde invalida
        hotelRoomQuery.withDateTo("14/02/2021");
        String invalidDateFrom = "14/02/2021";
        assertThatExceptionOfType(HotelRoomQueryException.class)
                .isThrownBy(() -> hotelRoomQuery.withDateFrom(invalidDateFrom))
                .withMessageContaining(HotelRoomQueryExceptionMessage.INVALID_DATE_FROM.getMessage());
    }

    @Test
    void testBookingBadRequests() {
        HotelRoomBookingRequestDTO invalidRequest = new HotelRoomBookingRequestDTO();
        Consumer<String> exceptionAsserter = message ->
                assertThatExceptionOfType(HotelRoomServiceException.class)
                .isThrownBy(() -> service.bookHotelRoom(invalidRequest))
                .withMessageContaining(message);
        // mail invalido
        exceptionAsserter.accept(INVALID_MAIL_FORMAT.getMessage());
        invalidRequest.setUserName("email@gmail.com");

        // Booking nulo
        exceptionAsserter.accept(EMPTY_BOOKING.getMessage());
        invalidRequest.setBooking(new BookingDTO());

        final String nullString = "null";
        // fechas invalidas
        // nulos
        BookingDTO bookingDTO = invalidRequest.getBooking();
        exceptionAsserter.accept(String.format(ERROR_MESSAGE, nullString));
        bookingDTO.setDateFrom("04/03/2021");
        // el to sigue siendo nulo
        exceptionAsserter.accept(String.format(ERROR_MESSAGE, nullString));

        // rango invalido
        bookingDTO.setDateTo("03/03/2021");
        exceptionAsserter.accept(DateRangeValidator.DateRangeError.INVALID_DATE_TO.getMessage());
        bookingDTO.setDateTo("04/04/2021");
        bookingDTO.setDateFrom("05/04/2021");
        exceptionAsserter.accept(DateRangeValidator.DateRangeError.INVALID_DATE_TO.getMessage()); // todo mismo mensaje
        bookingDTO.setDateFrom("04/03/2021");

        // Destino invalido
        // nulo
        exceptionAsserter.accept(String.format(LocationNotFoundException.MESSAGE, nullString));
        // invalido
        bookingDTO.setDestination("non-existent-destination");
        exceptionAsserter.accept(String.format(LocationNotFoundException.MESSAGE, "non-existent-destination"));
        bookingDTO.setDestination(BOGOTA.getLabel());

        // cdad de personas
        // lista de personas vacia
        exceptionAsserter.accept(EMPTY_PEOPLE_LIST.getMessage());
        bookingDTO.setPeople(new ArrayList<>());
        exceptionAsserter.accept(EMPTY_PEOPLE_LIST.getMessage());
        bookingDTO.getPeople().add(new PersonDTO());

        // cdad nula
        exceptionAsserter.accept(EMPTY_PEOPLE_AMOUNT.getMessage());
        // tipo de dato invalido
        bookingDTO.setPeopleAmount("not-an-int");
        exceptionAsserter.accept(INVALID_PEOPLE_AMOUNT_TYPE.getMessage());
        bookingDTO.setPeopleAmount("5.5");
        exceptionAsserter.accept(INVALID_PEOPLE_AMOUNT_TYPE.getMessage());
        // numero entero invalido
        bookingDTO.setPeopleAmount("0");
        exceptionAsserter.accept(INVALID_PEOPLE_AMOUNT.getMessage(0));
        bookingDTO.setPeopleAmount("-1");
        exceptionAsserter.accept(INVALID_PEOPLE_AMOUNT.getMessage(-1));
        // indica 2 y la lista tiene 1
        bookingDTO.setPeopleAmount("2");
        exceptionAsserter.accept(PEOPLE_AMOUNT_AND_PEOPLE_LIST_SIZE_MISMATCH.getMessage());
        bookingDTO.getPeople().add(new PersonDTO());

        // tipo de habitacion
        // nula
        exceptionAsserter.accept(EMPTY_ROOM_TYPE.getMessage());
        // invalida
        bookingDTO.setRoomType("not-a-room-type");
        exceptionAsserter.accept(String.format(RoomType.RoomTypeNotFoundException.MESSAGE, "not-a-room-type"));

        // no tiene capacidad para las personas indicadas (por ahora 2)
        bookingDTO.setRoomType(RoomType.SINGLE.getLabel());
        exceptionAsserter.accept(INVALID_ROOM_TYPE.getMessage());
        bookingDTO.setRoomType(RoomType.DOBLE.getLabel());

        // Medio de pago
        // nulo
        exceptionAsserter.accept(ServiceValidationError.EMPTY_PAYMENT_METHOD.getMessage());
        // no tiene numero
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        bookingDTO.setPaymentMethod(paymentMethodDTO);
        exceptionAsserter.accept(ServiceValidationError.EMPTY_CARD_NUMBER.getMessage());
        paymentMethodDTO.setNumber("1234");
        // no tiene tipo
        exceptionAsserter.accept(ServiceValidationError.EMPTY_PAYMENT_METHOD_TYPE.getMessage());
        // tipo invalido
        paymentMethodDTO.setType("not-a-pm-type");
        exceptionAsserter.accept(PaymentMethodType.PaymentMethodTypeError.PAYMENT_METHOD_TYPE_NOT_FOUND.getMsg("not-a-pm-type"));
        paymentMethodDTO.setType(PaymentMethodType.DEBIT.getLabel());
        // no tiene cuotas
        exceptionAsserter.accept(EMPTY_INSTALLMENTS.getMessage());
        paymentMethodDTO.setDues(3);

        Consumer<String> pmErrorAsserter = message ->
                assertThatExceptionOfType(PaymentMethodType.PaymentMethodTypeException.class)
                        .isThrownBy(() -> service.bookHotelRoom(invalidRequest))
                        .withMessageContaining(message);
        // no puede haber cuotas para ese medio de pago
        pmErrorAsserter.accept(PaymentMethodType.PaymentMethodTypeError.INSTALLMENTS_NOT_ALLOWED.getMsg());
        // numero de cuotas invalido
        paymentMethodDTO.setType(PaymentMethodType.CREDIT.getLabel());
        paymentMethodDTO.setDues(0);
        pmErrorAsserter.accept(PaymentMethodType.PaymentMethodTypeError.INVALID_INSTALLMENT_AMOUNT.getMsg(0));
        paymentMethodDTO.setDues(-1);
        pmErrorAsserter.accept(PaymentMethodType.PaymentMethodTypeError.INVALID_INSTALLMENT_AMOUNT.getMsg(-1));
        paymentMethodDTO.setDues(7);
        pmErrorAsserter.accept(PaymentMethodType.PaymentMethodTypeError.INVALID_INSTALLMENT_AMOUNT.getMsg(7));
    }
}