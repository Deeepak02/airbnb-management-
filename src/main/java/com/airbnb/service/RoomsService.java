package com.airbnb.service;

import com.airbnb.entity.AppUser;
import com.airbnb.entity.Booking;
import com.airbnb.entity.Property;
import com.airbnb.entity.Rooms;
import com.airbnb.exception.BookingFailedException;
import com.airbnb.exception.PropertyNotFound;
import com.airbnb.exception.RoomEmptyException;
import com.airbnb.payload.RoomsDto;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.RoomsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomsService {

    private RoomsRepository roomsRepository;
   private ModelMapper modelMapper;
    private  PropertyRepository propertyRepository;
    private BookingRepository bookingRepository;



    public RoomsService(RoomsRepository roomsRepository, ModelMapper modelMapper,
                        PropertyRepository propertyRepository, BookingRepository bookingRepository) {
        this.roomsRepository = roomsRepository;

        this.modelMapper = modelMapper;
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
    }

    RoomsDto mapToDto(Rooms rooms){
        return modelMapper.map(rooms,RoomsDto.class);
    }
    Rooms mapToEntity(RoomsDto roomsDto){
        return modelMapper.map(roomsDto,Rooms.class);

    }


    public RoomsDto saveRooms(RoomsDto roomsDto, Long propertyId) {
        Optional<Property> byPropertyId = propertyRepository.findById(propertyId);
        if (byPropertyId.isEmpty()) {
            throw new PropertyNotFound("Property by this ID not present: " + propertyId);
        }
        roomsDto.setProperty(byPropertyId.get());

        // Set the date if it's null (example: today's date)
        if (roomsDto.getDate() == null) {
            roomsDto.setDate(LocalDate.now());
        }

        Rooms rooms = mapToEntity(roomsDto);
        Rooms save = roomsRepository.save(rooms);
        return mapToDto(save);
    }





}
