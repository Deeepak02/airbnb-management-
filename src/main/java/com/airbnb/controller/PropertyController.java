package com.airbnb.controller;

import com.airbnb.payload.PropertyDto;
import com.airbnb.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {
    private PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping("/addproperty")
    public ResponseEntity<PropertyDto> createProperty(
            @RequestBody PropertyDto dto,
            @RequestParam Long cityId,
            @RequestParam Long countryId
    ){
     PropertyDto propertyDto=   propertyService.saveProperty(dto,cityId,countryId);
     return  new ResponseEntity<>(propertyDto, HttpStatus.CREATED);

    }


    @DeleteMapping("/deleteproperty")
    public ResponseEntity<String> deleteProperty(
            @RequestParam Long propertyId
    ){
       String result= propertyService.deletePropert(propertyId);

       if(result.contains("successfully")){
           return new ResponseEntity<>(result,HttpStatus.OK);
       }else {
           return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
       }

    }

    @PutMapping("/updateproperty")
    public ResponseEntity<PropertyDto> updateProperty(
            @RequestParam Long propertyId,
            @RequestParam Long cityId,
            @RequestParam Long countryId,
            @RequestBody PropertyDto dto
    ){
        PropertyDto propertyDto=  propertyService.updateProperty(propertyId,cityId,countryId,dto);
        return new ResponseEntity<>(propertyDto,HttpStatus.OK);
    }

    @GetMapping("/propertyresult")
     public ResponseEntity<List<PropertyDto>> searchProperty(
             @RequestParam("location") String location
     ){
     List<PropertyDto> propertyDtos=   propertyService.getSearchOnCityOrCountry(location);
     return new ResponseEntity<>(propertyDtos,HttpStatus.OK);

     }

}
