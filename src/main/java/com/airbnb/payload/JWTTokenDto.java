package com.airbnb.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTTokenDto {

    private String tokenType;
    private String token;
}
