package com.bootcamp.desafioquality.service.hotelroom.impl;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.BookingDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTOBuilder;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTOBuilder;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.repository.hotelroom.HotelRoomRepository;
import com.bootcamp.desafioquality.service.hotelroom.HotelRoomService;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceError;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceException;
import com.bootcamp.desafioquality.service.hotelroom.impl.exception.RoomNotAvailableException;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQuery;
import com.bootcamp.desafioquality.service.hotelroom.impl.validatedfields.HotelRoomValidFields;
import com.bootcamp.desafioquality.service.hotelroom.impl.validatedfields.HotelRoomValidatedFieldsProcessor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelRoomServiceImpl implements HotelRoomService {
    @Autowired
    HotelRoomRepository repository;

    @Override
    public List<HotelRoomResponseDTO> query(HotelRoomQuery hotelRoomQuery) {
        hotelRoomQuery.withAnyAvailability();
        return repository.listWhere(hotelRoomQuery.buildPredicate())
                .map(HotelRoomResponseDTOBuilder::build)
                .collect(Collectors.toList());
    }

    @Override
    public HotelRoomBookingResponseDTO bookHotelRoom(HotelRoomBookingRequestDTO requestDTO) {
        HotelRoomValidFields validatedFields = new HotelRoomValidatedFieldsProcessor().validate(requestDTO);
        HotelRoomBookingResponseDTOBuilder responseBuilder = new HotelRoomBookingResponseDTOBuilder(requestDTO);
        BookingDTO bookingDTO = requestDTO.getBooking();
        final HotelRoom hotelRoom = findHotelRoomOrFail(bookingDTO.getHotelCode(), validatedFields.getLocation());
        try {
            processHotelRoomReservation(validatedFields.getDateFrom(), validatedFields.getDateTo(), hotelRoom);
        } catch (RoomNotAvailableException e) {
            responseBuilder.withError(e.getMessage());
        }
        responseBuilder.withAmount(calculateAmount(validatedFields.getDateFrom(), validatedFields.getDateTo(), hotelRoom.getPrice(), validatedFields.getPeopleAmount()));
        responseBuilder.withInterest(validatedFields.getPaymentMethodValidatedFields().getInterest());
        return responseBuilder.build();
    }

    private double calculateAmount(Date dateFrom, Date dateTo, double price, int peopleAmount) {
        int days = DateParser.getDaysBetween(dateFrom, dateTo);
        return days * price * peopleAmount;
    }

    private void processHotelRoomReservation(Date dateFrom, Date dateTo, HotelRoom hotelRoom) throws RoomNotAvailableException {
        if (hotelRoom.hasRangeAvailable(dateFrom, dateTo)) {
            hotelRoom.reserve(dateFrom, dateTo);
        } else {
            throw new RoomNotAvailableException();
        }
    }

    private HotelRoom findHotelRoomOrFail(String hotelCode, Location location) {
        if (Strings.isBlank(hotelCode))
            throw new HotelRoomServiceException(HotelRoomServiceError.EMPTY_HOTEL_CODE.getMessage());
        Optional<HotelRoom> hotelRoomOpt = repository.find(hotelCode);
        if (hotelRoomOpt.isEmpty())
            throw new HotelRoomServiceException(HotelRoomServiceError.HOTEL_ROOM_NOT_FOUND.getMessage(hotelCode));
        HotelRoom hotelRoom = hotelRoomOpt.get();
        if (hotelRoom.getLocation() != location)
            throw new HotelRoomServiceException(HotelRoomServiceError.HOTEL_AND_LOCATION_MISTMACH.getMessage());
        return hotelRoom;
    }
}
