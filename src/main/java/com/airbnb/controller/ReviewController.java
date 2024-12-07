package com.airbnb.controller;

import com.airbnb.entity.AppUser;
import com.airbnb.entity.Review;
import com.airbnb.payload.AppUserDTO;
import com.airbnb.payload.ReviewDto;
import com.airbnb.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/createreview")
    public ResponseEntity<ReviewDto> createReview(
            @RequestBody Review review,
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam Long propertyId
    ){
      ReviewDto reviewDto1= reviewService.addReview(review,appUser,propertyId);
      return new ResponseEntity<>(reviewDto1, HttpStatus.CREATED);
    }

    @GetMapping("/userReviews")
    public List<Review> listReviewsOfUser(
            @AuthenticationPrincipal AppUser appUser
    ){

      List<Review> reviews=    reviewService.findReviewsByUser(appUser);
          return  reviews;


    }
}
