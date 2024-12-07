package com.airbnb.service;

import com.airbnb.entity.City;
import com.airbnb.entity.Country;
import com.airbnb.entity.Property;
import com.airbnb.payload.CityDto;
import com.airbnb.payload.PropertyDto;
import com.airbnb.repository.CityRepository;
import com.airbnb.repository.CountryRepository;
import com.airbnb.repository.PropertyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyService {
    private PropertyRepository propertyRepository;
    private ModelMapper modelMapper;
    private CityRepository cityRepository;
    private CountryRepository countryRepository;


    //    @Query("SELECT r FROM Rooms r WHERE r.property.id = :propertyId AND r.type = :roomType")
//    Rooms findByPropertyIdAndType(
//            @Param("propertyId") Long propertyId,
//            @Param("roomType") String roomType
//    );

    public PropertyService(PropertyRepository propertyRepository, ModelMapper modelMapper, CityRepository cityRepository, CountryRepository countryRepository) {
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

  PropertyDto  mapToDto(Property property){
     return    modelMapper.map(property,PropertyDto.class);

    }

  Property  mapToEntity(PropertyDto propertyDto){
       return modelMapper.map(propertyDto,Property.class);
    }
    public PropertyDto saveProperty(PropertyDto dto, Long cityId, Long countryId) {
        Property property = mapToEntity(dto);
        Optional<City> opCity = cityRepository.findById(cityId);
        if (opCity.isEmpty()) {
            throw new IllegalArgumentException("City not found with ID: " + cityId);
        }
        property.setCity(opCity.get());


        Optional<Country> countryOptional = countryRepository.findById(countryId);
        if (countryOptional.isEmpty()) {
            throw new IllegalArgumentException("Country not found with ID: " + countryId);
        }
        property.setCountry(countryOptional.get());

        Property savedProperty = propertyRepository.save(property);
        PropertyDto propertyDto = mapToDto(savedProperty);
        return propertyDto;

    }

    public String deletePropert(Long propertyId) {
        Optional<Property> opProperty = propertyRepository.findById(propertyId);
        if (opProperty.isPresent()){
            propertyRepository.deleteById(propertyId);
            return "Property with ID " + propertyId + " has been deleted successfully.";
        }else {
            return "Property with ID " + propertyId + " is not available.";

        }

    }

    public PropertyDto updateProperty(Long propertyId, PropertyDto dto) {
        Property property = propertyRepository.findById(propertyId).get();

        Property property1 = mapToEntity(dto);
        property1.setId(propertyId);
        Property save = propertyRepository.save(property1);
        PropertyDto propertyDto = mapToDto(save);
        return propertyDto;
    }

    public PropertyDto updateProperty(Long propertyId, Long cityId, Long countryId, PropertyDto dto) {
        // Fetch the existing property
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));

        // Fetch associated entities
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new IllegalArgumentException("City not found with ID: " + cityId));
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with ID: " + countryId));

        // Update property fields from the DTO
        if (dto.getName() != null) {
            property.setName(dto.getName());
        }
        if (dto.getNumberOfGuests() != null) {
            property.setNumberOfGuests(dto.getNumberOfGuests());
        }
        if (dto.getNumberOfBeds() != null) {
            property.setNumberOfBeds(dto.getNumberOfBeds());
        }
        if (dto.getNumberOfBathroom() != null) {
            property.setNumberOfBathroom(dto.getNumberOfBathroom());
        }
        if (dto.getNumberOfBedrooms() != null) {
            property.setNumberOfBedrooms(dto.getNumberOfBedrooms());
        }

        // Update the city and country relationships
        property.setCity(city);
        property.setCountry(country);

        // Save the updated property
        Property updatedProperty = propertyRepository.save(property);

        // Convert and return the updated property as a DTO
        return mapToDto(updatedProperty);
    }

    public List<PropertyDto> getSearchOnCityOrCountry(String location) {
        List<Property> properties = propertyRepository.searchProperty(location);
        List<PropertyDto> dto = properties.stream().map(e -> mapToDto(e)).collect(Collectors.toList());
        return dto;
    }


}



