package com.airbnb.controller;

import com.airbnb.payload.RoomsDto;
import com.airbnb.service.RoomsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomsController {
    private RoomsService roomsService;

    public RoomsController(RoomsService roomsService) {
        this.roomsService = roomsService;
    }


    @PostMapping("/add-rooms")
    public ResponseEntity<RoomsDto> addRooms(
            @RequestBody RoomsDto roomsDto,
            @RequestParam Long propertyId
    ){
       RoomsDto roomsDto1= roomsService.saveRooms(roomsDto,propertyId);
       return new ResponseEntity<>(roomsDto, HttpStatus.CREATED);

    }
}
