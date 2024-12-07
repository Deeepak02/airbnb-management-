package com.airbnb.payload;

import com.airbnb.entity.AppUser;
import com.airbnb.entity.Property;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    private Long id;
    private Integer rating;
    private String description;
    private AppUser appUser;
    private Property property;
}
