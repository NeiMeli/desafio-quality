package com.bootcamp.desafioquality.controller.hotelroom;

import com.bootcamp.desafioquality.controller.ExceptionHandlerController;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.service.hotelroom.HotelRoomService;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
public class HotelRoomController implements ExceptionHandlerController {

    @Autowired
    HotelRoomService service;

    @GetMapping
    public List<HotelRoomResponseDTO> queryAvailable(@RequestParam(required = false) String dateFrom,
                                                     @RequestParam (required = false) String dateTo,
                                                     @RequestParam (required = false) String[] destination) {
        HotelRoomQuery hotelRoomQuery = new HotelRoomQuery();
        hotelRoomQuery.withDateFrom(dateFrom);
        hotelRoomQuery.withDateTo(dateTo);
        hotelRoomQuery.withDestinations(destination);
        return service.query(hotelRoomQuery);
    }



}
