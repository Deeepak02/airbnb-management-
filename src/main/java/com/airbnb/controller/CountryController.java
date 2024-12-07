package com.airbnb.controller;

import com.airbnb.payload.CityDto;
import com.airbnb.payload.CountryDto;
import com.airbnb.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/country")
public class CountryController {
    private CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping("/addcountry")
    public ResponseEntity<CountryDto> createCountry(
            @RequestBody CountryDto dto
    ){
       CountryDto countryDto= countryService.saveCountry(dto);
       return new ResponseEntity<>(countryDto, HttpStatus.CREATED);

    }

    @DeleteMapping("/deletecountry")
    public ResponseEntity<String> deleteCountry(
            @RequestParam Long countryId
    ){
            String result= countryService.deleteCountry(countryId);

            if (result.contains("successfully")){
                return new ResponseEntity<>(result,HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
            }

    }


    @PutMapping("/updatecountry")
    public ResponseEntity<CountryDto> updateCountry(
            @RequestParam Long countryId,
            @RequestBody CountryDto dto
    ){
        CountryDto countryDto=  countryService.updateCountry(countryId,dto);
        return new ResponseEntity<>(countryDto,HttpStatus.OK);
    }
}
