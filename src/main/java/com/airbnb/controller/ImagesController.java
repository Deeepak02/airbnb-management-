package com.airbnb.controller;

import com.airbnb.entity.AppUser;
import com.airbnb.entity.Images;
import com.airbnb.entity.Property;
import com.airbnb.repository.ImagesRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.service.BucketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/images")
public class ImagesController {

    private BucketService bucketService;
    private PropertyRepository propertyRepository;
    private ImagesRepository imagesRepository;

    public ImagesController(BucketService bucketService, PropertyRepository propertyRepository, ImagesRepository imagesRepository) {
        this.bucketService = bucketService;
        this.propertyRepository = propertyRepository;
        this.imagesRepository = imagesRepository;
    }

    @PostMapping(path="/upload/file/{bucketName}/property/{propertyId}",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFile(
            @RequestParam MultipartFile file,
            @PathVariable String bucketName,
            @PathVariable Long propertyId,
            @AuthenticationPrincipal AppUser user
            ) throws IOException {
        String imageUrl = bucketService.uploadFile(file, bucketName);
        Property property = propertyRepository.findById(propertyId).get();
    Images img  =new   Images();
    img.setUrl(imageUrl);
    img.setProperty(property);

        Images savedImage = imagesRepository.save(img);
        return new ResponseEntity<>(savedImage, HttpStatus.OK);

    }
}
