package com.bootcamp.desafioquality.service.hotelroom.impl.query;

public class HotelRoomQueryException extends RuntimeException {
    public HotelRoomQueryException(String message) {
        super(message);
    }

    public enum HotelRoomQueryExceptionMessage {
        INVALID_DATE_TO("Fecha hasta invalida: %s"),
        INVALID_DATE_FROM("Fecha desde invalida: %s");

        private final String message;

        HotelRoomQueryExceptionMessage(String message) {
            this.message = message;
        }

        public String getMessage(Object ... args) {
            return String.format(message, args);
        }
    }
}
