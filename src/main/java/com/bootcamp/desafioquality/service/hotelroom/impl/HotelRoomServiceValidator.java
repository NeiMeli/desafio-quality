package com.bootcamp.desafioquality.service.hotelroom.impl;

import com.bootcamp.desafioquality.entity.hotel.RoomType;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceException;
import com.bootcamp.desafioquality.service.validation.ServiceValidator;
import org.jetbrains.annotations.Nullable;

import static com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceError.INVALID_ROOM_TYPE;

public class HotelRoomServiceValidator extends ServiceValidator {
    @Nullable private Integer personAmount = null;
    @Nullable private RoomType roomType = null;

    public HotelRoomServiceValidator() {
        super(HotelRoomServiceException::new);
    }

    @Override
    public void validatePeopleAmount(String peopleAmount) {
        super.validatePeopleAmount(peopleAmount);
        int intAmount = Integer.parseInt(peopleAmount);
        if (roomType != null) validateRoomCapacity(intAmount, roomType);
        this.personAmount = intAmount;
    }

    public void validateRoomType(@Nullable String roomTypeString) {
        if (roomTypeString == null) {
            throw exceptionSupplier.apply(INVALID_ROOM_TYPE.getMessage());
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
}
