package com.bootcamp.desafioquality.service.hotelroom.impl;

import com.bootcamp.desafioquality.common.CacheDBTableMock;
import com.bootcamp.desafioquality.controller.hotelroom.dto.response.HotelRoomResponseDTO;
import com.bootcamp.desafioquality.entity.hotel.HotelRoom;
import com.bootcamp.desafioquality.repository.hotelroom.impl.HotelRoomCacheRespository;
import com.bootcamp.desafioquality.service.hotelroom.impl.query.HotelRoomQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bootcamp.desafioquality.common.HotelRoomTestConstants.DATABASE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class HotelRoomServiceImplTest {
    @MockBean
    HotelRoomCacheRespository repository;

    @Autowired
    HotelRoomServiceImpl service;

    @Test
    void testListAllAvailable() {
        List<HotelRoom> hotelRoomList = DATABASE.get();
        when(repository.getDatabase())
                .thenReturn(new CacheDBTableMock<>(hotelRoomList));
        when(repository.listWhere(any())).thenCallRealMethod();

        HotelRoomQuery hotelRoomQuery = new HotelRoomQuery();
        int totalSize = hotelRoomList.size(); // primero están todas disponibles
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS).hasSize(totalSize);

        // reservo 5
        List<String> reservedRooms = new ArrayList<>();
        IntStream.range(0, 4).forEach(index -> {
            HotelRoom hotelRoom = hotelRoomList.get(index);
            hotelRoom.reserve();
            reservedRooms.add(hotelRoom.getCode());
        });

        int availableSizeAfterReserve = totalSize - reservedRooms.size();
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS2 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS2).hasSize(availableSizeAfterReserve);

        // me aseguro de que sólo me devuelva las disponibles
        assertThat(hotelRoomResponseDTOS2).noneMatch(dto -> reservedRooms.contains(dto.getCode()));
    }

    @Test
    void testQueryCases() {
        List<HotelRoom> hotelRoomList = DATABASE.get();
        when(repository.getDatabase())
                .thenReturn(new CacheDBTableMock<>(hotelRoomList));
        when(repository.listWhere(any())).thenCallRealMethod();
        HotelRoomQuery hotelRoomQuery = new HotelRoomQuery();

        // primero testeo una query que no traiga nada
        hotelRoomQuery.withDestinations("non-existent-destination");
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS1 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS1).isEmpty();

        // un destino
        final String bsas = "Buenos Aires";
        final String cartagena = "Cartagena";
        hotelRoomQuery.withDestinations(bsas);
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS2 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS2).hasSize(2);
        assertThat(hotelRoomResponseDTOS2).allMatch(dto -> dto.getLocation().equals(bsas));

        // dos destinos
        hotelRoomQuery.withDestinations(bsas, cartagena);
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS3 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS3).hasSize(3);
        assertThat(hotelRoomResponseDTOS2).allMatch(dto -> dto.getLocation().equals(bsas) || dto.getLocation().equals(cartagena));

        // si los reservo, no tienen que aparecer
        List<String> codes = hotelRoomResponseDTOS3.stream().map(HotelRoomResponseDTO::getCode).collect(Collectors.toList());
        hotelRoomList.stream().filter(hr -> codes.contains(hr.getCode()))
                .forEach(HotelRoom::reserve);
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS4 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS4).isEmpty();

        // fecha desde
        hotelRoomQuery.withoutDestinations();
        hotelRoomQuery.withDateFrom("12/02/2021");
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS5 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS5).hasSize(6);

        // fecha desde y hasta
        hotelRoomQuery.withDateTo("27/03/2021");
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS6 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS6).hasSize(2);

        // todos los de hotelRoomResponseDTOS6 tienen que cumplir tambien el desde.
        assertThat(hotelRoomResponseDTOS6.stream().map(HotelRoomResponseDTO::getCode))
                .allMatch(code -> hotelRoomResponseDTOS5.stream().map(HotelRoomResponseDTO::getCode).collect(Collectors.toList()).contains(code));

        // solo hasta
        hotelRoomQuery.withoutDateFrom();
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS7 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS7).hasSize(5);

        // agrego desde y tambien ubicacion
        final String bogota = "Bogotá";
        hotelRoomQuery.withDateFrom("23/01/2021");
        hotelRoomQuery.withDestinations(bogota);
        List<HotelRoomResponseDTO> hotelRoomResponseDTOS8 = service.query(hotelRoomQuery);
        assertThat(hotelRoomResponseDTOS8).hasSize(2);
        assertThat(hotelRoomResponseDTOS8).allMatch(dto -> dto.getLocation().equals(bogota));
    }
}