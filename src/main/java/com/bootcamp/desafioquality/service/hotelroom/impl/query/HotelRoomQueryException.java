package com.bootcamp.desafioquality.service.hotelroom.impl.query;

public class HotelRoomQueryException extends RuntimeException {
    public HotelRoomQueryException(String message) {
        super(message);
    }

    public enum HotelRoomQueryExceptionMessage {
        INVALID_DATE_TO("La fecha de salida debe ser mayor a la de entrada"),
        INVALID_DATE_FROM("La fecha de entrada debe ser menor a la de salida");

        private final String message;

        HotelRoomQueryExceptionMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
