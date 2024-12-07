package com.airbnb.repository;

import com.airbnb.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {



    @Query("SELECT r FROM Rooms r WHERE r.property.id = :propertyId AND r.type = :type AND r.date = :date")
    Rooms findByPropertyIdAndTypeAndDate(
            @Param("propertyId") Long propertyId,
            @Param("type") String type,
            @Param("date") LocalDate date
    );


}