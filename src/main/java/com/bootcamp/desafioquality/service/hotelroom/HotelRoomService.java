package com.bootcamp.desafioquality.service.hotelroom;

import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQuery;

import java.util.List;

public interface HotelRoomService {
    List<HotelRoomResponseDTO> query(HotelRoomQuery hotelRoomQuery);
    HotelRoomBookingResponseDTO bookHotelRoom(HotelRoomBookingRequestDTO requestDTO);
}
