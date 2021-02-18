package com.bootcamp.desafioquality.service.hotelroom;

import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;

import java.util.List;

public interface HotelRoomService {
    List<HotelRoomResponseDTO> listAllAvailable();
}
