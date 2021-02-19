package com.bootcamp.desafioquality.service.hotelroom.impl;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.BookingDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.bootcamp.desafioquality.entity.hotel.RoomType;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceException;
import com.bootcamp.desafioquality.service.validation.ServiceValidationError;
import com.bootcamp.desafioquality.service.validation.ServiceValidator;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceError.*;

public class HotelRoomServiceValidator extends ServiceValidator {
    @Nullable private Integer personAmount = null;
    @Nullable private RoomType roomType = null;

    public HotelRoomServiceValidator() {
        super(HotelRoomServiceException::new);
    }

    @Override
    public void validatePeopleAmount(String peopleAmountParameter, int actualPeopleAmount) {
        super.validatePeopleAmount(peopleAmountParameter, actualPeopleAmount);
        int intAmount = Integer.parseInt(peopleAmountParameter);
        if (roomType != null) validateRoomCapacity(intAmount, roomType);
        this.personAmount = intAmount;
    }

    public void validateRoomType(@Nullable String roomTypeString) {
        if (roomTypeString == null) {
            throw exceptionSupplier.apply(EMPTY_ROOM_TYPE.getMessage());
        }
        final RoomType roomType = RoomType.fromLabelOrElseThrow(roomTypeString, () -> exceptionSupplier.apply(String.format(RoomType.RoomTypeNotFoundException.MESSAGE, roomTypeString)));
        if (personAmount != null) validateRoomCapacity(personAmount, roomType);
        this.roomType = roomType;
    }

    private void validateRoomCapacity(int personAmount, RoomType roomType) {
        if (!roomType.hasCapacity(personAmount)) {
            throw exceptionSupplier.apply(INVALID_ROOM_TYPE.getMessage());
        }
    }
    
    public void validateBooking(@Nullable BookingDTO bookingDTO) {
        if (bookingDTO == null) {
            throw exceptionSupplier.apply(EMPTY_BOOKING.getMessage());
        }
        validateDates(bookingDTO.getDateFrom(), bookingDTO.getDateTo());
        validateLocation(bookingDTO.getDestination());
        List<PersonDTO> people = bookingDTO.getPeople();
        if (people == null || people.isEmpty()) {
            throw exceptionSupplier.apply(ServiceValidationError.EMPTY_PEOPLE_LIST.getMessage());
        }
        validatePeopleAmount(bookingDTO.getPeopleAmount(), people.size());
        validateRoomType(bookingDTO.getRoomType());
        validatePaymentMethod(bookingDTO.getPaymentMethod());
    }

}
