package com.airbnb.service;

import com.airbnb.entity.AppUser;
import com.airbnb.entity.Booking;
import com.airbnb.entity.Property;
import com.airbnb.entity.Rooms;
import com.airbnb.exception.BookingFailedException;
import com.airbnb.exception.PropertyNotFound;
import com.airbnb.exception.RoomEmptyException;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.RoomsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private PropertyRepository propertyRepository;
    private RoomsRepository roomsRepository;
    private BookingRepository bookingRepository;
    private PDFService pdfService;
    private  SmsService smsService;


    public BookingService(PropertyRepository propertyRepository, RoomsRepository roomsRepository, BookingRepository bookingRepository, PDFService pdfService, SmsService smsService) {
        this.propertyRepository = propertyRepository;
        this.roomsRepository = roomsRepository;
        this.bookingRepository = bookingRepository;
        this.pdfService = pdfService;
        this.smsService = smsService;
    }

    public Rooms createBooking(Long propertyId, String roomType, Booking booking, AppUser appUser) {
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);
        if (propertyOptional.isEmpty()) {
            throw new PropertyNotFound("Property by this ID not present: " + propertyId);
        }

        Property property = propertyOptional.get();
        List<LocalDate> datesBetween = getDatesBetween(booking.getCheckInDate(), booking.getCheckOutDate());

        // Check room availability for each date
        for (LocalDate date : datesBetween) {
            Rooms room = roomsRepository.findByPropertyIdAndTypeAndDate(propertyId, roomType, date);
            if (room == null || room.getCount() == 0) {
                throw new RoomEmptyException("No rooms available for date: " + date);
            }
        }

        // Fetch room details for pricing
        Rooms room = roomsRepository.findByPropertyIdAndTypeAndDate(propertyId, roomType, booking.getCheckInDate());
        if (room == null) {
            throw new PropertyNotFound("Room type not found for the given property");
        }

        // Calculate total price based on nightly rates for each date
        float total = 0;
        for (LocalDate date : datesBetween) {
            Rooms currentRoom = roomsRepository.findByPropertyIdAndTypeAndDate(propertyId, roomType, date);
            if (currentRoom != null) {
                total += currentRoom.getPrice();
            }
        }

        // Set booking details
        booking.setTotal_price(total);
        booking.setProperty(property);
        booking.setAppUser(appUser);

        Booking savedBooking = bookingRepository.save(booking);
        if (savedBooking != null) {
            for (LocalDate date : datesBetween) {
                Rooms bookedRoom = roomsRepository.findByPropertyIdAndTypeAndDate(propertyId, roomType, date);
                if (bookedRoom != null) {
                    bookedRoom.setCount(bookedRoom.getCount() - 1);
                    roomsRepository.save(bookedRoom);
                }
            }
            // Generate PDF Document
            pdfService.generateAndSendPdf(savedBooking);

            // Send SMS Confirmation
            smsService.sendSms("+91" + booking.getMobile(), "Your booking is confirmed. Your booking id is: " + booking.getId());

            // Return the room successfully
            return room;
        }

        // If execution reaches here, something unexpected happened
        throw new BookingFailedException("Failed to create booking");
    }


    public static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return dates;
    }
}
