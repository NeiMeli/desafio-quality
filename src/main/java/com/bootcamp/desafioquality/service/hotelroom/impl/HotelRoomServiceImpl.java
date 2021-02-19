package com.bootcamp.desafioquality.service.hotelroom.impl;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.BookingDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTOBuilder;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTOBuilder;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.repository.hotelroom.HotelRoomRepository;
import com.bootcamp.desafioquality.service.hotelroom.HotelRoomService;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceError;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceException;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQuery;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.GregorianCalendar;
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
        validate(requestDTO);
        HotelRoomBookingResponseDTOBuilder responseBuilder = new HotelRoomBookingResponseDTOBuilder(requestDTO);
        BookingDTO bookingDTO = requestDTO.getBooking();
        final HotelRoom hotelRoom = findHotelRoomOrFail(bookingDTO.getHotelCode());
        // todo validar si el destino coincide con el hotel
        try {
            processHotelRoomReservation(bookingDTO, hotelRoom);
            responseBuilder.withAmount(calculateAmount(bookingDTO, hotelRoom.getPrice()));
        } catch (RoomNotAvailableException e) {
            responseBuilder.withError(e.getMessage());
        }
        return responseBuilder.build();
    }

    private double calculateAmount(BookingDTO bookingDTO, double price) {
        int days = DateParser.getDaysBetween(bookingDTO.getDateFrom(), bookingDTO.getDateTo());
        return days * price * Integer.parseInt(bookingDTO.getPeopleAmount());
    }

    private HotelRoom findHotelRoomOrFail(String hotelCode) {
        if (Strings.isBlank(hotelCode))
            throw new HotelRoomServiceException(HotelRoomServiceError.EMPTY_HOTEL_CODE.getMessage());
        Optional<HotelRoom> hotelRoomOpt = repository.find(hotelCode);
        if (hotelRoomOpt.isEmpty())
            throw new HotelRoomServiceException(HotelRoomServiceError.HOTEL_ROOM_NOT_FOUND.getMessage(hotelCode));
        return hotelRoomOpt.get();
    }

    private void processHotelRoomReservation(BookingDTO bookingDTO, HotelRoom hotelRoom) throws RoomNotAvailableException {
        // si llego hasta aca las fechas tienen que ser validas
        Date dateFrom = DateParser.fromString(bookingDTO.getDateFrom());
        Date dateTo = DateParser.fromString(bookingDTO.getDateTo());
        if (hotelRoom.hasRangeAvailable(dateFrom, dateTo)) {
            hotelRoom.reserve(dateFrom, dateTo);
        } else {
            throw new RoomNotAvailableException();
        }
    }

    private void validate(HotelRoomBookingRequestDTO requestDTO) {
        final HotelRoomServiceValidator validator = new HotelRoomServiceValidator();
        validator.validateEmail(requestDTO.getUserName());
        validator.validateBooking(requestDTO.getBooking());
    }
}
