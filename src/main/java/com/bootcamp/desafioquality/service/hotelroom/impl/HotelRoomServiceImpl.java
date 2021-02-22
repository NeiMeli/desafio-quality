package com.bootcamp.desafioquality.service.hotelroom.impl;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.BookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.*;
import com.bootcamp.desafioquality.date.DateParser;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.entity.hotel.RoomType;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.repository.hotelroom.HotelRoomRepository;
import com.bootcamp.desafioquality.service.hotelroom.HotelRoomService;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceError;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceException;
import com.bootcamp.desafioquality.service.hotelroom.impl.exception.RoomNotAvailableException;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQuery;
import com.bootcamp.desafioquality.service.hotelroom.impl.validfields.HotelRoomValidFields;
import com.bootcamp.desafioquality.service.hotelroom.impl.validfields.HotelRoomValidFieldsProcessor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bootcamp.desafioquality.service.util.RoundUtil.roundTwoDecimals;

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
        HotelRoomValidFields validFields = new HotelRoomValidFieldsProcessor().validate(requestDTO);
        HotelRoomBookingResponseDTOBuilder responseBuilder = new HotelRoomBookingResponseDTOBuilder(requestDTO, validFields);
        BookingRequestDTO bookingRequestDTO = requestDTO.getBooking();
        String hotelCode = bookingRequestDTO.getHotelCode();
        final HotelRoom hotelRoom = findHotelRoomOrFail(hotelCode, validFields.getDestination(), validFields.getRoomType());
        responseBuilder.withHotelCode(hotelCode);
        try {
            processHotelRoomReservation(validFields.getDateFrom(), validFields.getDateTo(), hotelRoom);
        } catch (RoomNotAvailableException e) {
            responseBuilder.withError(e.getMessage());
            return responseBuilder.build();
        }
        responseBuilder.withAmount(calculateAmount(validFields.getDateFrom(), validFields.getDateTo(), hotelRoom.getPrice(), validFields.getPeopleAmount()));
        responseBuilder.withInterest(validFields.getPaymentMethodValidatedFields().getInterest());
        return responseBuilder.build();
    }

    private double calculateAmount(Date dateFrom, Date dateTo, double price, int peopleAmount) {
        int days = DateParser.getDaysBetween(dateFrom, dateTo);
        return roundTwoDecimals(days * price * peopleAmount);
    }

    private void processHotelRoomReservation(Date dateFrom, Date dateTo, HotelRoom hotelRoom) throws RoomNotAvailableException {
        if (hotelRoom.hasRangeAvailable(dateFrom, dateTo)) {
            hotelRoom.reserve(dateFrom, dateTo);
        } else {
            throw new RoomNotAvailableException();
        }
    }

    private HotelRoom findHotelRoomOrFail(String hotelCode, Location location, RoomType roomType) {
        if (Strings.isBlank(hotelCode))
            throw new HotelRoomServiceException(HotelRoomServiceError.EMPTY_HOTEL_CODE.getMessage());
        HotelRoomQuery query = new HotelRoomQuery();
        query.withHotelCode(hotelCode);
        query.withDestinations(List.of(location));
        query.withRoomType(roomType);
        Optional<HotelRoom> hotelRoomOpt = repository.findFirst(query.buildPredicate());
        if (hotelRoomOpt.isEmpty())
            throw new HotelRoomServiceException(HotelRoomServiceError.HOTEL_ROOM_NOT_FOUND.getMessage());
        return hotelRoomOpt.get();
    }
}
