package com.bootcamp.desafioquality.service.flight.validfields;

import com.bootcamp.desafioquality.controller.flight.dto.FlightReservationDTO;
import com.bootcamp.desafioquality.controller.flight.dto.request.FlightReservationRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.bootcamp.desafioquality.entity.flight.SeatType;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.service.flight.exception.FlightServiceException;
import com.bootcamp.desafioquality.service.validation.fields.CommonValidFields;
import com.bootcamp.desafioquality.service.validation.processor.CommonValidFieldsProcessor;

import java.util.List;

public class FlightValidFieldsProcessor extends CommonValidFieldsProcessor {
    private final FlightValidFields validFields;

    public FlightValidFieldsProcessor() {
        super(FlightServiceException::new);
        validFields = new FlightValidFields(FlightServiceException::new);
    }


    @Override
    protected CommonValidFields getValidatedFields() {
        return validFields;
    }

    public FlightValidFields validate(FlightReservationRequestDTO reservationDTO) {
        validateEmail(reservationDTO.getUserName());
        validateFlightReservation(reservationDTO.getFlightReservation());
        return validFields;
    }

    private void validateFlightReservation(FlightReservationDTO flightReservation) {
        validateDates(flightReservation.getDateFrom(), flightReservation.getDateTo());
        validateOrigin(flightReservation.getOrigin());
        validateDestination(flightReservation.getDestination());
        List<PersonDTO> people = flightReservation.getPeople();
        validatePeopleList(flightReservation.getPeople());
        validatePeopleAmount(flightReservation.getSeats(), people.size());
        validateSeatType(flightReservation.getSeatType());
        validatePaymentMethod(flightReservation.getPaymentMethod());
    }

    private void validateOrigin(String origin) {
        Location location = validateLocation(origin);
        validFields.setOrigin(location);
    }

    private void validateSeatType(String seatType) {
        SeatType st = SeatType.fromLabelOrElseThrow(seatType, exceptionSupplier);
        validFields.setSeatType(st);
    }
}
