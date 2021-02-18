package com.bootcamp.desafioquality.service.hotelroom.impl;

import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTOBuilder;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.repository.hotelroom.HotelRoomRepository;
import com.bootcamp.desafioquality.service.hotelroom.HotelRoomService;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelRoomServiceImpl implements HotelRoomService {
    @Autowired
    HotelRoomRepository repository;

    @Override
    public List<HotelRoomResponseDTO> query(HotelRoomQuery hotelRoomQuery) {
        hotelRoomQuery.withAvailability();
        return repository.listWhere(hotelRoomQuery.buildPredicate())
                .map(HotelRoomResponseDTOBuilder::build)
                .collect(Collectors.toList());
    }
}
