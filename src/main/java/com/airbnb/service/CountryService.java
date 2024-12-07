package com.airbnb.service;

import com.airbnb.entity.City;
import com.airbnb.entity.Country;
import com.airbnb.payload.CityDto;
import com.airbnb.payload.CountryDto;
import com.airbnb.repository.CountryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryService {
    private CountryRepository countryRepository;
    private ModelMapper modelMapper;

    public CountryService(CountryRepository countryRepository, ModelMapper modelMapper) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
    }

 CountryDto   mapToDto(Country country){
     return   modelMapper.map(country,CountryDto.class);
 }


Country mapToEntity(CountryDto countryDto){
          return modelMapper.map(countryDto,Country.class);
 }

    public CountryDto saveCountry(CountryDto dto) {
        Country country = mapToEntity(dto);
        Country save = countryRepository.save(country);
        CountryDto countryDto = mapToDto(save);
        return countryDto;

    }

    public String deleteCountry(Long countryId) {
        Optional<Country> opCountry = countryRepository.findById(countryId);

        if(opCountry.isPresent()){
            countryRepository.deleteById(countryId);
            return "Country with ID " + countryId + " has been deleted successfully.";
        }else {
            return "Country with ID " + countryId + " is not available.";

        }
    }

    public CountryDto updateCountry(Long countryId, CountryDto dto) {

        Country country = countryRepository.findById(countryId).get();

        Country country1 = mapToEntity(dto);
        country1.setId(countryId);
        Country save = countryRepository.save(country1);
        CountryDto countryDto = mapToDto(save);
        return countryDto;
    }
}
