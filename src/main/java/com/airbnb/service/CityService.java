package com.airbnb.service;

import com.airbnb.entity.City;
import com.airbnb.payload.CityDto;
import com.airbnb.repository.CityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityService {

    private CityRepository cityRepository;
    private ModelMapper modelMapper;

    public CityService(CityRepository cityRepository, ModelMapper modelMapper) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
    }

  CityDto  mapToDto(City city){
        return modelMapper.map(city,CityDto.class);
  }
     City      mapToEntity(CityDto dto){
      return   modelMapper.map(dto,City.class);
     }

    public CityDto saveCity(CityDto dto) {

        City city = mapToEntity(dto);
        City save = cityRepository.save(city);
        CityDto cityDto = mapToDto(save);
        return cityDto;

    }


    public String deleteCity(long cityId) {
        Optional<City> opCity = cityRepository.findById(cityId);
        if(opCity.isPresent()){
            cityRepository.deleteById(cityId);
            return "City with ID " + cityId + " has been deleted successfully.";
        } else {
            return "City with ID " + cityId + " is not available.";

        }

    }

    public CityDto updateCity(Long cityId, CityDto dto) {
        City city = cityRepository.findById(cityId).get();

        City city1 = mapToEntity(dto);
        city1.setId(cityId);
        City save = cityRepository.save(city1);
        CityDto cityDto = mapToDto(save);
        return cityDto;
    }
}
