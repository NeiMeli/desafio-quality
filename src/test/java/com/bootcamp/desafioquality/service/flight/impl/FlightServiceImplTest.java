package com.bootcamp.desafioquality.service.flight.impl;

import com.bootcamp.desafioquality.controller.common.dto.response.StatusCodeDTO;
import com.bootcamp.desafioquality.controller.flight.dto.request.FlightReservationRequestDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTOBuilder;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightResponseDTO;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.date.DateRangeValidator;
import com.bootcamp.desafioquality.entity.flight.Flight;
import com.bootcamp.desafioquality.entity.flight.SeatType;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType;
import com.bootcamp.desafioquality.service.flight.exception.FlightServiceError;
import com.bootcamp.desafioquality.service.flight.exception.FlightServiceException;
import com.bootcamp.desafioquality.service.flight.query.FlightQuery;
import com.bootcamp.desafioquality.service.validation.error.FieldProcessorError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.bootcamp.desafioquality.common.FlightTestConstants.*;
import static com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType.CREDIT;
import static com.bootcamp.desafioquality.entity.paymentmethod.PaymentMethodType.DEBIT;
import static com.bootcamp.desafioquality.service.validation.error.FieldProcessorError.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class FlightServiceImplTest {
    @Autowired
    FlightServiceImpl service;

   @Test
   void testQueryCases() {
       List<Flight> flightList = DATABASE.get();
       FlightQuery query = new FlightQuery();

       // listAll (query vacia)
       List<String> dbCodes = flightList.stream().map(Flight::getCode).collect(Collectors.toList());
       List<FlightResponseDTO> all = service.query(query);
       assertThat(all).hasSize(flightList.size());
       assertThat(all.stream().map(FlightResponseDTO::getCode))
               .allMatch(dbCodes::contains);

       // Origen
       final String bsas = "Buenos Aires";
       query.withOrigin(bsas);
       List<FlightResponseDTO> results1 = service.query(query);
       assertThat(results1).allMatch(r -> r.getOrigin().equalsIgnoreCase(bsas));

       // destino
       query.withoutOrigin();
       final String tucuman = "Tucumán";
       query.withDestination(tucuman);
       List<FlightResponseDTO> results2 = service.query(query);
       assertThat(results2).allMatch(r -> r.getDestination().equalsIgnoreCase(tucuman));

       // origen y destino
       query.withOrigin(bsas);
       List<FlightResponseDTO> results3 = service.query(query);
       assertThat(results3).allMatch(r -> r.getOrigin().equalsIgnoreCase(bsas) && r.getDestination().equalsIgnoreCase(tucuman));

        // fecha desde
       query.withoutOrigin();
       query.withoutDestination();
       final String validDateFrom = "10/02/2021";
       query.withDateFrom(validDateFrom);
       List<FlightResponseDTO> results4 = service.query(query);
       assertThat(results4).allMatch(r -> r.getDateFrom().equalsIgnoreCase(validDateFrom));

       // fecha hasta
       query.withoutDateFrom();
       final String validDateTo = "17/02/2021";
       query.withDateTo(validDateTo);
       List<FlightResponseDTO> results5 = service.query(query);
       assertThat(results5).allMatch(r -> r.getDateTo().equalsIgnoreCase(validDateTo));

       // fecha desde y hasta
       query.withDateFrom(validDateFrom);
       List<FlightResponseDTO> results6 = service.query(query);
       assertThat(results6).allMatch(r -> r.getDateFrom().equalsIgnoreCase(validDateFrom) && r.getDateTo().equalsIgnoreCase(validDateTo));

       // tipo de asiento
       query.withoutDateFrom();
       query.withoutDateTo();
       final String validSeatType = "Economy";
       query.withSeatType(validSeatType);
       List<FlightResponseDTO> results7 = service.query(query);
       assertThat(results7).allMatch(r -> r.getSeatType().equalsIgnoreCase(validSeatType));

       // todos los filtros
       query.withOrigin(bsas)
               .withDestination(tucuman)
               .withDateFrom(validDateFrom)
               .withDateTo(validDateTo);
       List<FlightResponseDTO> results8 = service.query(query);
       assertThat(results8).allMatch(r ->
                               r.getOrigin().equalsIgnoreCase(bsas) && r.getDestination().equalsIgnoreCase(tucuman) &&
                               r.getDateFrom().equalsIgnoreCase(validDateFrom) && r.getDateTo().equalsIgnoreCase(validDateTo) &&
                               r.getSeatType().equalsIgnoreCase(validSeatType));

       // una query que no traiga nada
       FlightQuery query2 = new FlightQuery().withDateTo("17/02/2028");
       assertThat(service.query(query2)).isEmpty();
   }

   @Test
   void testFlightReservationHappy() {
       FlightReservationRequestDTO request = VALID_RESERVATION_REQUEST.get();
       FlightReservationResponseDTO response = service.reserveFlight(request);
       StatusCodeDTO statusCode = response.getStatusCode();
       assertThat(statusCode.getCode()).isEqualTo(HttpStatus.OK.value());
       assertThat(statusCode.getMessage()).isEqualTo(FlightReservationResponseDTOBuilder.SUCCESS_MESSAGE);

       // me aseguro que calcule bien el precio
       assertThat(response.getAmount()).isEqualTo(14640d);
       assertThat(response.getInterest()).isEqualTo(5d);
       assertThat(response.getTotal()).isEqualTo(15372d);
   }

  @Test
  void testFlightReservationNotFound() {
      FlightReservationRequestDTO request = VALID_RESERVATION_REQUEST.get();
      request.getFlightReservation().setFlightNumber("another-number");
      FlightReservationResponseDTO response = service.reserveFlight(request);
      StatusCodeDTO statusCode = response.getStatusCode();
      assertThat(statusCode.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
      assertThat(statusCode.getMessage()).isEqualTo(FlightServiceError.FLIGHT_NOT_FOUND.getMessage());
      assertThat(response.getAmount()).isNull();
      assertThat(response.getInterest()).isNull();
      assertThat(response.getTotal()).isNull();
  }

  @Test
  void testFlightReservationBadRequests() {
      BiConsumer<FlightReservationRequestDTO, String> exceptionAsserter = (request, message) ->
              assertThatExceptionOfType(FlightServiceException.class)
                      .isThrownBy(() -> service.reserveFlight(request))
                      .withMessageContaining(message);
      Consumer<FlightReservationRequestDTO> resetReservation = dto -> dto.setFlightReservation(VALID_RESERVATION_DETAIL_REQUEST_DTO_1.get());
      // email invalido
      exceptionAsserter.accept(VALID_RESERVATION_REQUEST.get().setUserName("invalid-mail"), FieldProcessorError.INVALID_MAIL_FORMAT.getMessage());

      // formato de fecha invalido
      // desde
      FlightReservationRequestDTO r1 = VALID_RESERVATION_REQUEST.get();
      final String invalidDateFormat = "invalid-date-format";
      r1.getFlightReservation().setDateFrom(invalidDateFormat);
      exceptionAsserter.accept(r1, String.format(DateParser.ERROR_MESSAGE, invalidDateFormat));

      // hasta
      resetReservation.accept(r1);
      r1.getFlightReservation().setDateTo(invalidDateFormat);
      exceptionAsserter.accept(r1, String.format(DateParser.ERROR_MESSAGE, invalidDateFormat));

      // rango de fechas invalido
      resetReservation.accept(r1);
      r1.getFlightReservation().setDateFrom("05/04/2021");
      r1.getFlightReservation().setDateTo("04/04/2021");
      exceptionAsserter.accept(r1, DateRangeValidator.DateRangeError.INVALID_DATE_TO.getMessage());

      // asiento invalido
      resetReservation.accept(r1);
      final String invalidSeatType = "not-a-seatType";
      r1.getFlightReservation().setSeatType(invalidSeatType);
      exceptionAsserter.accept(r1, String.format(SeatType.SeatTypeNotFoundException.MESSAGE, invalidSeatType));

      // personas
      // cdad invalida
      r1.getFlightReservation().setSeats("5n");
      exceptionAsserter.accept(r1, INVALID_PEOPLE_AMOUNT_TYPE.getMessage());
      r1.getFlightReservation().setSeats("0");
      exceptionAsserter.accept(r1, INVALID_PEOPLE_AMOUNT.getMessage("0") );
      r1.getFlightReservation().setSeats("-1");
      exceptionAsserter.accept(r1, INVALID_PEOPLE_AMOUNT.getMessage("-1") );
      resetReservation.accept(r1);

      // lista de personas vacia
      r1.getFlightReservation().setPeople(new ArrayList<>());
      exceptionAsserter.accept(r1, EMPTY_PEOPLE_LIST.getMessage());

      // la lista de personas no coincide con la cantidad indicada
      resetReservation.accept(r1);
      r1.getFlightReservation().setSeats("5");
      exceptionAsserter.accept(r1, PEOPLE_AMOUNT_AND_PEOPLE_LIST_SIZE_MISMATCH.getMessage());
      r1.getFlightReservation().setSeats("2");

      // origen invalido
      final String invalidLocation = "not-a-location";
      r1.getFlightReservation().setOrigin(invalidLocation);
      exceptionAsserter.accept(r1, String.format(Location.LocationNotFoundException.MESSAGE, invalidLocation));

      // destino invalido
      resetReservation.accept(r1);
      r1.getFlightReservation().setDestination(invalidLocation);
      exceptionAsserter.accept(r1, String.format(Location.LocationNotFoundException.MESSAGE, invalidLocation));

      // datos de pasajeros invalidos
      // dni
      resetReservation.accept(r1);
      final String invalidDni = "not-a-dni";
      r1.getFlightReservation().getPeople().get(0).setDni(invalidDni);
      exceptionAsserter.accept(r1, String.format(INVALID_DNI_VALUE.getMessage(invalidDni), invalidDni));
      resetReservation.accept(r1);

      // email
      r1.getFlightReservation().getPeople().get(0).setMail("not-a-mail");
      exceptionAsserter.accept(r1, INVALID_MAIL_FORMAT.getMessage());

      // medio de pago
      // tipo invalido
      resetReservation.accept(r1);
      String invalidPmType = "not-a-payment-method-type";
      r1.getFlightReservation().getPaymentMethod().setType(invalidPmType);
      exceptionAsserter.accept(r1, PaymentMethodType.PaymentMethodTypeError.PAYMENT_METHOD_TYPE_NOT_FOUND.getMsg( invalidPmType));

      // cuotas invalidas para el tipo
      r1.getFlightReservation().getPaymentMethod().setType(DEBIT.getLabel());
      assertThatExceptionOfType(PaymentMethodType.PaymentMethodTypeException.class)
              .isThrownBy(() -> service.reserveFlight(r1))
              .withMessageContaining(PaymentMethodType.PaymentMethodTypeError.INSTALLMENTS_NOT_ALLOWED.getMsg());

      r1.getFlightReservation().getPaymentMethod().setType(CREDIT.getLabel());
      r1.getFlightReservation().getPaymentMethod().setDues(7);
      assertThatExceptionOfType(PaymentMethodType.PaymentMethodTypeException.class)
              .isThrownBy(() -> service.reserveFlight(r1))
              .withMessageContaining(PaymentMethodType.PaymentMethodTypeError.INVALID_INSTALLMENT_AMOUNT.getMsg(7));
  }
}