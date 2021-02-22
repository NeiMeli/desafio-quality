package com.bootcamp.desafioquality.service.hotelroom.impl.validfields;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.BookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.PersonDTO;
import com.bootcamp.desafioquality.entity.hotel.RoomType;
import com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceException;
import com.bootcamp.desafioquality.service.validation.error.FieldProcessorError;
import com.bootcamp.desafioquality.service.validation.fields.CommonValidFields;
import com.bootcamp.desafioquality.service.validation.processor.CommonValidFieldsProcessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.bootcamp.desafioquality.service.hotelroom.exception.HotelRoomServiceError.*;

public class HotelRoomValidFieldsProcessor extends CommonValidFieldsProcessor {
    private final HotelRoomValidFields validatedFields;
    public HotelRoomValidFieldsProcessor() {
        super(HotelRoomServiceException::new);
        this.validatedFields = new HotelRoomValidFields(HotelRoomServiceException::new);
    }

    @Override
    protected CommonValidFields getValidatedFields() {
        return this.validatedFields;
    }

    @Override
    public void validatePeopleAmount(String peopleAmountParameter, int actualPeopleAmount) {
        super.validatePeopleAmount(peopleAmountParameter, actualPeopleAmount);
        if (validatedFields.hasRoomType()) validateRoomCapacity(validatedFields.getPeopleAmount(), validatedFields.getRoomType());
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

    public void validateBooking(BookingRequestDTO bookingRequestDTO) {
        if (bookingRequestDTO == null) {
            throw exceptionSupplier.apply(EMPTY_BOOKING.getMessage());
        }
        validateDates(bookingRequestDTO.getDateFrom(), bookingRequestDTO.getDateTo());
        validateDestination(bookingRequestDTO.getDestination());
        List<PersonDTO> people = bookingRequestDTO.getPeople();
        if (people == null || people.isEmpty()) {
            throw exceptionSupplier.apply(FieldProcessorError.EMPTY_PEOPLE_LIST.getMessage());
        }
        validatePeopleAmount(bookingRequestDTO.getPeopleAmount(), people.size());
        validateRoomType(bookingRequestDTO.getRoomType());
        validatePaymentMethod(bookingRequestDTO.getPaymentMethod());
        validatePeopleList(bookingRequestDTO.getPeople());
    }

    public HotelRoomValidFields validate(HotelRoomBookingRequestDTO requestDTO) {
        validateEmail(requestDTO.getUserName());
        validateBooking(requestDTO.getBooking());
        return validatedFields;
    }
}
