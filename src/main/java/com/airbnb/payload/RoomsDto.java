package com.airbnb.payload;

import com.airbnb.entity.Property;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class RoomsDto {
    private String type;
    private Float price;
    private Integer count;
    private Property property;

    private LocalDate date;
}
