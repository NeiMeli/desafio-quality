package com.bootcamp.desafioquality.service.hotelroom.impl;

import com.bootcamp.desafioquality.common.CacheDBTableMock;
import com.bootcamp.desafioquality.common.HotelRoomTestConstants;
import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.BookingDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PaymentMethodDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTOBuilder;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.date.DateRange;
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
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bootcamp.desafioquality.common.HotelRoomTestConstants.DATABASE;
import static com.bootcamp.desafioquality.date.DateParser.ERROR_MESSAGE;
import static com.bootcamp.desafioquality.entity.location.Location.*;
import static com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceError.*;
import static com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQueryException.HotelRoomQueryExceptionMessage;
import static com.bootcamp.desafioquality.service.validation.ServiceValidationError.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
            hotelRoom.reserve(hotelRoom.getNextAvailableRange().getDateFrom(), hotelRoom.getNextAvailableRange().getDateTo());
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
                .forEach(hr -> {
                    DateRange nextAvailableRange = hr.getNextAvailableRange();
                    hr.reserve(nextAvailableRange.getDateFrom(), nextAvailableRange.getDateTo());
                });
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

        // hago una reserva parcial de uno, el from deberia seguir trayendo resultados
        hotelRoomList.stream()
                .filter(hr -> hr.getCode().equals("SE-0001"))
                .findFirst()
                .orElseThrow()
                .reserve(DateParser.fromString("14/06/2021"), DateParser.fromString( "15/07/2021"));
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS8_2 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS8_2).hasSize(2);
        assertThat(hotelRoomResponseDTOS8_2).anyMatch(hr -> hr.getCode().equals("SE-0001"));

        // pero si pongo el to dentro del rango que reserve, ya no me lo va a encontrar
        hotelRoomQuery.withDateTo("28/06/2021");
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS8_3 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS8_3).noneMatch(hr -> hr.getCode().equals("SE-0001"));

        // incluso si el to es posterior, la reserva en el medio me lo inhabilita
        hotelRoomQuery.withDateTo("28/08/2021");
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS8_4 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS8_4).noneMatch(hr -> hr.getCode().equals("SE-0001"));

        // ahora si saco el from, el to que habia puesto me lo hace posible
        hotelRoomQuery.withoutDateFrom();
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS8_5 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS8_5).anyMatch(hr -> hr.getCode().equals("SE-0001"));

        // si pongo el from dentro del rango reservado, otra vez no obtengo nada
        hotelRoomQuery.withDateFrom("28/06/2021");
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS8_6 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS8_6).noneMatch(hr -> hr.getCode().equals("SE-0001"));

        // saco el to, pero aun asi no esta disponible en esa fecha
        hotelRoomQuery.withoutDateTo();
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS8_7 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS8_7).noneMatch(hr -> hr.getCode().equals("SE-0001"));

        // muevo el from para adelante, obtengo resultados
        hotelRoomQuery.withDateFrom("28/08/2021");
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS8_8 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS8_8).anyMatch(hr -> hr.getCode().equals("SE-0001"));

        // le agrego un to mas adelante, obtengo resultados
        hotelRoomQuery.withDateTo("10/09/2021");
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS8_9 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS8_9).anyMatch(hr -> hr.getCode().equals("SE-0001"));

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
        bookingDTO.setDestination(BS_AS.getLabel());

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
        paymentMethodDTO.setDues(3);

        List<HotelRoom> hotelRoomList = DATABASE.get();
        when(repository.getDatabase())
                .thenReturn(new CacheDBTableMock<>(hotelRoomList));
        when(repository.listWhere(any())).thenCallRealMethod();
        when(repository.find(anyString())).thenCallRealMethod();

        // codigo de hotel
        // nulo
        exceptionAsserter.accept(EMPTY_HOTEL_CODE.getMessage());
        // inexistente
        bookingDTO.setHotelCode("not-a-hotel-room");
        exceptionAsserter.accept(HOTEL_ROOM_NOT_FOUND.getMessage("not-a-hotel-room"));

        // hotel y ubicacion no coinciden
        bookingDTO.setHotelCode("SE-0001");
        exceptionAsserter.accept(HOTEL_AND_LOCATION_MISTMACH.getMessage());
    }

    @Test
    void testBookingHappy() {
        List<HotelRoom> hotelRoomList = DATABASE.get();
        when(repository.getDatabase())
                .thenReturn(new CacheDBTableMock<>(hotelRoomList));
        when(repository.find(anyString())).thenCallRealMethod();

        HotelRoomBookingRequestDTO request = HotelRoomTestConstants.VALID_BOOKING_REQUEST.get();
        HotelRoomBookingResponseDTO response = service.bookHotelRoom(request);
        StatusCodeDTO statusCode = response.getStatusCode();
        assertThat(statusCode.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(statusCode.getMessage()).isEqualTo(HotelRoomBookingResponseDTOBuilder.SUCCESS_MESSAGE);

        assertThat(response.getAmount()).isEqualTo(390000d);
        assertThat(response.getInterest()).isEqualTo(20d);
        assertThat(response.getTotal()).isEqualTo(468000d);

        // tengo que asgurarme que la habitacion quedó sin disponibilidad en esa fecha
        BookingDTO booking = request.getBooking();
        HotelRoom hotelRoom = repository.find(booking.getHotelCode()).orElseThrow();
        assertThat(hotelRoom.hasRangeAvailable(DateParser.fromString(booking.getDateFrom()), DateParser.fromString(booking.getDateTo()))).isFalse();
        // si hago el mismo request, va a dar error
        HotelRoomBookingResponseDTO response2 = service.bookHotelRoom(request);
        StatusCodeDTO statusCode2 = response2.getStatusCode();
        assertThat(statusCode2.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(statusCode2.getMessage()).isEqualTo(RoomNotAvailableException.MESSAGE);

        assertThat(response2.getAmount()).isNull();
        assertThat(response2.getInterest()).isNull();
        assertThat(response2.getTotal()).isNull();

        // si hago una reserva mas adelante tengo que poder
        booking.setDateFrom(booking.getDateTo());
        booking.setDateTo("17/06/2021");
        HotelRoomBookingResponseDTO response3 = service.bookHotelRoom(request);
        StatusCodeDTO statusCode3 = response3.getStatusCode();
        assertThat(statusCode3.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(statusCode3.getMessage()).isEqualTo(HotelRoomBookingResponseDTOBuilder.SUCCESS_MESSAGE);

    }

    @Test
    void testBookingRoomNotAvailable() {
        List<HotelRoom> hotelRoomList = DATABASE.get();
        when(repository.getDatabase())
                .thenReturn(new CacheDBTableMock<>(hotelRoomList));
        when(repository.find(anyString())).thenCallRealMethod();

        HotelRoomBookingRequestDTO request = HotelRoomTestConstants.VALID_BOOKING_REQUEST.get();
        request.getBooking().setDateTo("27/07/2050");
        HotelRoomBookingResponseDTO response = service.bookHotelRoom(request);
        StatusCodeDTO statusCode = response.getStatusCode();
        assertThat(statusCode.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(statusCode.getMessage()).isEqualTo(RoomNotAvailableException.MESSAGE);

        assertThat(response.getAmount()).isNull();
        assertThat(response.getInterest()).isNull();
        assertThat(response.getTotal()).isNull();
    }
}