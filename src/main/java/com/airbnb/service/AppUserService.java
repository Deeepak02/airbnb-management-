package com.airbnb.service;

import com.airbnb.entity.AppUser;
import com.airbnb.exception.UserExists;
import com.airbnb.payload.AppUserDTO;
import com.airbnb.payload.LoginDto;
import com.airbnb.repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class AppUserService {

    private  AppUserRepository appUserRepository;
    private  ModelMapper modelMapper;
    private JWTService jwtService;

    public AppUserService(AppUserRepository appUserRepository, ModelMapper modelMapper, JWTService jwtService) {
        this.appUserRepository = appUserRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
    }

     AppUserDTO mapToDto(AppUser appUser) {
       return modelMapper.map(appUser, AppUserDTO.class);
    }

     AppUser mapToEntity(AppUserDTO dto) {
        return modelMapper.map(dto, AppUser.class);
    }

    //USER
    public AppUserDTO saveUser(AppUserDTO dto) {
        // Check for existing email
        Optional<AppUser> opEmail = appUserRepository.findByEmail(dto.getEmail());
        if (opEmail.isPresent()) {
            throw new UserExists("Email ID already exists");
        }

        // Check for existing username
        Optional<AppUser> opUsername = appUserRepository.findByUsername(dto.getUsername());
        if (opUsername.isPresent()) {
            throw new UserExists("Username already exists");
        }

        dto.setRole("ROLE_USER");


        // Hash password and set it in DTO
        String hashedPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10));
        dto.setPassword(hashedPassword);


        // Convert DTO to entity and save
        AppUser appUser = mapToEntity(dto);
        AppUser savedAppUser = appUserRepository.save(appUser);

        // Convert saved entity to DTO and return

        AppUserDTO appUserDTO = mapToDto(savedAppUser);
        return appUserDTO;

//        return mapToDto(savedAppUser);
    }

    //OWNER
    public AppUserDTO savePropertyOwner(AppUserDTO dto) {
        Optional<AppUser> opEmail = appUserRepository.findByEmail(dto.getEmail());
        if(opEmail.isPresent()){
            throw new UserExists("Email ID already exists");
        }

        Optional<AppUser> opUsername = appUserRepository.findByUsername(dto.getUsername());
        if (opUsername.isPresent()) {
            throw new UserExists("Username already exists");
        }
        // Hash password and set it in DTO
        String hashedPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10));
        dto.setPassword(hashedPassword);

        dto.setRole("ROLE_OWNER");

        // Convert DTO to entity and save
        AppUser appUser = mapToEntity(dto);
        AppUser savedAppUser = appUserRepository.save(appUser);

        // Convert saved entity to DTO and return

        AppUserDTO appUserDTO = mapToDto(savedAppUser);
        return appUserDTO;
    }

    //ADMIN
    public AppUserDTO saveAdmin(AppUserDTO dto) {
        Optional<AppUser> opEmail = appUserRepository.findByEmail(dto.getEmail());
        if (opEmail.isPresent()){
            throw new UserExists("Email Id already Exists");
        }
        Optional<AppUser> opUsername = appUserRepository.findByUsername(dto.getUsername());
        if (opUsername.isPresent()){
            throw new UserExists("Username Already exists");
        }
        String hashedPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10));
        dto.setPassword(hashedPassword);

        dto.setRole("ROLE_ADMIN");
        // Convert DTO to entity and save
        AppUser appUser = mapToEntity(dto);
        AppUser savedAppUser = appUserRepository.save(appUser);

        // Convert saved entity to DTO and return

        AppUserDTO appUserDTO = mapToDto(savedAppUser);
        return appUserDTO;

    }

    //MANAGER
    public AppUserDTO savePropertyManager(AppUserDTO dto) {
        Optional<AppUser> opEmail = appUserRepository.findByEmail(dto.getEmail());
        if (opEmail.isPresent()){
            throw new UserExists("Email Id Already exists");
        }
        Optional<AppUser> opUsername = appUserRepository.findByUsername(dto.getUsername());
        if(opUsername.isPresent()){
            throw new UserExists("Username Already exists");
        }
        String hashedPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10));
        dto.setPassword(hashedPassword);

        dto.setRole("ROLE_MANAGER");
        AppUser appUser = mapToEntity(dto);
        AppUser saveAppUser = appUserRepository.save(appUser);
        AppUserDTO appUserDTO = mapToDto(saveAppUser);
        return appUserDTO;
    }


    //login with username
//    public boolean verifyLogin(LoginDto loginDto) {
//
//        Optional<AppUser> opUser = appUserRepository.findByUsername(loginDto.getUsername());
//        if(opUser.isPresent()){
//            AppUser appUser = opUser.get();
//      return       BCrypt.checkpw(loginDto.getPassword(),appUser.getPassword());//decrypt that user password compair to login password after check it will return boolean value
//
//        }
//        return false;
//    }

    public Boolean loginWithEmail(LoginDto loginDto) {

        Optional<AppUser> opEmail = appUserRepository.findByEmail(loginDto.getEmail());
        if(opEmail.isPresent()){
            AppUser appUser = opEmail.get();
          return   BCrypt.checkpw(loginDto.getPassword(),appUser.getPassword());

        }
        return false;

    }

    public String verifyLogin(LoginDto loginDto) {
        Optional<AppUser> opEmailOrUserName = appUserRepository.findByEmailOrUsername(loginDto.getEmail(), loginDto.getUsername());
    if (opEmailOrUserName.isPresent()){

        AppUser appUser = opEmailOrUserName.get();
      if(   BCrypt.checkpw(loginDto.getPassword(),appUser.getPassword())){
          return  jwtService.generateToken(appUser);

      }
    }
        return  null;

    }



}
