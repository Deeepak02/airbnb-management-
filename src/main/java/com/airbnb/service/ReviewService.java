package com.airbnb.service;

import com.airbnb.entity.AppUser;
import com.airbnb.entity.Property;
import com.airbnb.entity.Review;
import com.airbnb.exception.PropertyAlreadyExistsException;
import com.airbnb.exception.ReviewAlreadyExistsException;
import com.airbnb.payload.AppUserDTO;
import com.airbnb.payload.ReviewDto;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private ReviewRepository reviewRepository;
    private PropertyRepository propertyRepository;
    private ModelMapper modelMapper;

    public ReviewService(ReviewRepository reviewRepository, PropertyRepository propertyRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper;
    }

   ReviewDto mapToDto(Review review){
        return modelMapper.map(review,ReviewDto.class);

    }

   Review mapToEntity(ReviewDto reviewDto){
        return modelMapper.map(reviewDto,Review.class);

    }

    public ReviewDto addReview(Review review, AppUser appUser, Long propertyId) {
        Optional<Property> opProperty = propertyRepository.findById(propertyId);
        if (opProperty.isEmpty()) {
            throw new PropertyAlreadyExistsException("Property not found with ID: " + propertyId);
        }

        Property property = opProperty.get();
        Optional<Review> reviewDetails = reviewRepository.findByAppUserAndProperty(appUser, property);
        if (reviewDetails.isPresent()) {
            throw new ReviewAlreadyExistsException("Review already exists for this user and property.");
        }

        review.setAppUser(appUser);
        review.setProperty(property);
        Review savedReview = reviewRepository.save(review);
        return mapToDto(savedReview);

    }

    public List<Review> findReviewsByUser(AppUser appUser) {
      return  reviewRepository.findReviewsByUser(appUser);


    }
}
