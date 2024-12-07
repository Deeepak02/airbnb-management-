package com.airbnb.controller;

import com.airbnb.entity.AppUser;
import com.airbnb.entity.Booking;
import com.airbnb.entity.Rooms;
import com.airbnb.service.BookingService;;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    private BookingService bookingService;


    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping("/createBooking")
    public ResponseEntity<Rooms> createBooking(
            @RequestParam Long propertyId,
            @RequestParam String roomType,
            @RequestBody Booking booking,
            @AuthenticationPrincipal AppUser appUser
            ){
        Rooms bookedRoom = bookingService.createBooking(propertyId, roomType,booking,appUser);
        return new ResponseEntity<>(bookedRoom, HttpStatus.CREATED);

    }


}
