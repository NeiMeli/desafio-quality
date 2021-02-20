package com.bootcamp.desafioquality.service.hotelroom.impl;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.BookingDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.bootcamp.desafioquality.entity.hotel.RoomType;
import com.bootcamp.desafioquality.entity.location.Location;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceException;
import com.bootcamp.desafioquality.service.validation.ServiceValidationError;
import com.bootcamp.desafioquality.service.validation.ValidatedFieldsProvider;
import com.bootcamp.desafioquality.service.validation.ValidatedFields;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceError.*;

public class HotelRoomValidatedFieldsProvider extends ValidatedFieldsProvider {
    private final HotelRoomValidatedFields validatedFields;
    public HotelRoomValidatedFieldsProvider() {
        super(HotelRoomServiceException::new);
        this.validatedFields = new HotelRoomValidatedFields(HotelRoomServiceException::new);
    }

    @Override
    protected ValidatedFields getValidatedFields() {
        return this.validatedFields;
    }

    @Override
    public void validatePeopleAmount(String peopleAmountParameter, int actualPeopleAmount) {
        super.validatePeopleAmount(peopleAmountParameter, actualPeopleAmount);
        if (validatedFields.hasRoomType()) validateRoomCapacity(validatedFields.getPeopleAmount(), validatedFields.getRoomType());
    }

    @Override
    public void validateLocation(String location) {
        super.validateLocation(location);
        validatedFields.setLocation(Location.fromLabel(location));
    }

    public void validateRoomType(@Nullable String roomTypeString) {
        if (roomTypeString == null) {
            throw exceptionSupplier.apply(EMPTY_ROOM_TYPE.getMessage());
        }
        final RoomType roomType = RoomType.fromLabelOrElseThrow(roomTypeString, () -> exceptionSupplier.apply(String.format(RoomType.RoomTypeNotFoundException.MESSAGE, roomTypeString)));
        if (validatedFields.hasPeopleAmount()) validateRoomCapacity(validatedFields.getPeopleAmount(), roomType);
        validatedFields.setRoomType(roomType);
    }

    private void validateRoomCapacity(int peopleAmount, RoomType roomType) {
        if (!roomType.hasCapacity(peopleAmount)) {
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

    public HotelRoomValidatedFields validate(HotelRoomBookingRequestDTO requestDTO) {
        validateEmail(requestDTO.getUserName());
        validateBooking(requestDTO.getBooking());
        return validatedFields;
    }
}
