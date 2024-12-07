package com.airbnb.controller;

import com.airbnb.payload.AppUserDTO;
import com.airbnb.payload.JWTTokenDto;
import com.airbnb.payload.LoginDto;
import com.airbnb.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AppUserService appUserService;

    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }


    @PostMapping("/createuser")
    public ResponseEntity<AppUserDTO> createUser(
            @RequestBody AppUserDTO dto
            ){
       AppUserDTO appUserDTO= appUserService.saveUser(dto);


       return new ResponseEntity<>(appUserDTO, HttpStatus.CREATED);

    }

    @PostMapping("/createpropertyowner")
    public ResponseEntity<AppUserDTO>  createPropertyOwner(
            @RequestBody AppUserDTO dto
    ){
     AppUserDTO appUserDTO =  appUserService.savePropertyOwner(dto);
     return new ResponseEntity<>(appUserDTO,HttpStatus.CREATED);

    }

    @PostMapping("/createadmin")
    public ResponseEntity<AppUserDTO> createAdmin(
            @RequestBody AppUserDTO dto
    ){
      AppUserDTO appUserDTO=  appUserService.saveAdmin(dto);
      return new ResponseEntity<>(appUserDTO,HttpStatus.CREATED);

    }

    @PostMapping("/createpropertymanager")
    public ResponseEntity<AppUserDTO> createPropertyManager(
            @RequestBody AppUserDTO dto
    ){
      AppUserDTO appUserDTO=  appUserService.savePropertyManager(dto);

      return new ResponseEntity<>(appUserDTO,HttpStatus.CREATED);

    }

    //Login with username
//    @PostMapping("/login")
//    public ResponseEntity<String> signIn(
//            @RequestBody LoginDto loginDto
//    ){
//
//      boolean status=  appUserService.verifyLogin(loginDto);
//        if (status){
//            return new ResponseEntity<>("Successful",HttpStatus.OK);
//        }
//        else {
//            return new ResponseEntity<>("Invalid username/password",HttpStatus.UNAUTHORIZED);
//        }
//    }


    //Login with Email
    @PostMapping("/loginByEmail")
    public ResponseEntity<String> loginByEmail(
            @RequestBody LoginDto loginDto
    ){

       Boolean status= appUserService.loginWithEmail(loginDto);
       if (status){
           return new ResponseEntity<>("Successfully Login by email",HttpStatus.OK);
       }
       else {
           return new ResponseEntity<>("Invalid username/password",HttpStatus.UNAUTHORIZED);
       }
    }





    // login username or email

    @PostMapping("/login")
    public ResponseEntity<?> signIn(
            @RequestBody LoginDto loginDto
    ){
       String token= appUserService.verifyLogin(loginDto);
        JWTTokenDto jwtToken=new JWTTokenDto();

       if (token!=null){
           jwtToken.setTokenType("JWT");
           jwtToken.setToken(token);
           return new ResponseEntity<>(jwtToken,HttpStatus.OK);
       }
       else {
           return new ResponseEntity<>("Invalid username/password",HttpStatus.UNAUTHORIZED);
       }

    }
}
