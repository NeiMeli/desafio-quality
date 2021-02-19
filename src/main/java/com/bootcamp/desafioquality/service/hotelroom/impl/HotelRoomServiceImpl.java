package com.bootcamp.desafioquality.service.hotelroom.impl;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.BookingDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTOBuilder;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.date.DateRangeValidator;
import com.bootcamp.desafioquality.repository.hotelroom.HotelRoomRepository;
import com.bootcamp.desafioquality.service.validation.EmailValidator;
import com.bootcamp.desafioquality.service.hotelroom.HotelRoomService;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceError;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceException;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelRoomServiceImpl implements HotelRoomService {
    @Autowired
    HotelRoomRepository repository;

    @Override
    public List<HotelRoomResponseDTO> query(HotelRoomQuery hotelRoomQuery) {
        hotelRoomQuery.withAvailability();
        return repository.listWhere(hotelRoomQuery.buildPredicate())
                .map(HotelRoomResponseDTOBuilder::build)
                .collect(Collectors.toList());
    }

    @Override
    public HotelRoomBookingResponseDTO bookHotelRoom(HotelRoomBookingRequestDTO requestDTO) {
        validate(requestDTO);
        return null;
    }

    private void validate(HotelRoomBookingRequestDTO requestDTO) {
        HotelRoomServiceValidator validator = new HotelRoomServiceValidator();
        validator.validateEmail(requestDTO.getUserName());
        final BookingDTO booking = requestDTO.getBooking();
        validator.validateDates(booking.getDateFrom(), booking.getDateTo());
        validator.validateLocation(booking.getDestination());
        validator.validatePeopleAmount(booking.getPeopleAmount());
        validator.validateRoomType(booking.getRoomType());
    }

    private void validateDestination(String destination) {

    }

    private void validateEmail(String email) {
        if (!EmailValidator.isEmailValid(email)) {
            throw new HotelRoomServiceException(HotelRoomServiceError.INVALID_MAIL_FORMAT.getMessage());
        }
    }

    private void validateDates(String dateFromString, String dateToString) {
        Date dateFrom = DateParser.fromString(dateFromString);
        Date dateTo = DateParser.fromString(dateToString);
        DateRangeValidator dateRangeValidator = new DateRangeValidator(HotelRoomServiceException::new);
        dateRangeValidator.validateDateFrom(dateFrom);
        dateRangeValidator.validateDateTo(dateTo);
    }
}
