package com.bootcamp.desafioquality.controller.hotelroom;

import com.bootcamp.desafioquality.controller.ExceptionHandlerController;
import com.bootcamp.desafioquality.controller.hotelroom.dto.request.HotelRoomBookingRequestDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomBookingResponseDTO;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.service.hotelroom.HotelRoomService;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQuery;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @RequestMapping("/booking")
    public HotelRoomBookingResponseDTO bookHotelRoom(@RequestBody @NotNull HotelRoomBookingRequestDTO requestDTO) {
        return service.bookHotelRoom(requestDTO);
    }

}
