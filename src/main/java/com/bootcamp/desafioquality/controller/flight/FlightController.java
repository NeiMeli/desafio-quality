package com.bootcamp.desafioquality.controller.flight;

import com.bootcamp.desafioquality.controller.ExceptionHandlerController;
import com.bootcamp.desafioquality.controller.flight.dto.request.FlightReservationRequestDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightReservationResponseDTO;
import com.bootcamp.desafioquality.controller.flight.dto.response.FlightResponseDTO;
import com.bootcamp.desafioquality.service.flight.FlightService;
import com.bootcamp.desafioquality.service.flight.query.FlightQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController implements ExceptionHandlerController {
    @Autowired
    FlightService service;

    @GetMapping
    public List<FlightResponseDTO> query(@RequestParam(required = false) String dateFrom,
                                         @RequestParam (required = false) String dateTo,
                                         @RequestParam (required = false) String origin,
                                         @RequestParam (required = false) String destination,
                                         @RequestParam (required = false) String seatType) {
        FlightQuery query = new FlightQuery();
        query.withDateFrom(dateFrom)
                .withDateTo(dateTo)
                .withOrigin(origin)
                .withDestination(destination)
                .withSeatType(seatType);
        return service.query(query);
    }

    @PostMapping("/flight-reservation")
    public FlightReservationResponseDTO reserve(@Valid @RequestBody FlightReservationRequestDTO reservationRequest) {
        return service.reserveFlight(reservationRequest);
    }
}
