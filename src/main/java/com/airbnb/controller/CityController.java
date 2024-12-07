package com.airbnb.controller;

import com.airbnb.payload.CityDto;
import com.airbnb.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/city")
public class CityController {

    private CityService cityService;


    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("/addcity")
    public ResponseEntity<CityDto> createCity(
            @RequestBody CityDto dto
    ){
     CityDto cityDto=   cityService.saveCity(dto);
     return new ResponseEntity<>(cityDto, HttpStatus.CREATED);

    }
    @DeleteMapping("/deletecity")
    public ResponseEntity<String> deleteCity(
            @RequestParam long cityId
    ) {
        String result = cityService.deleteCity(cityId);
        if (result.contains("successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/updatecity")
    public ResponseEntity<CityDto> updateCity(
            @RequestParam Long cityId,
            @RequestBody CityDto dto
    ){
         CityDto cityDto=  cityService.updateCity(cityId,dto);
         return new ResponseEntity<>(cityDto,HttpStatus.OK);
    }
}
