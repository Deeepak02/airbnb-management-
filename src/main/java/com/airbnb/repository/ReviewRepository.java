package com.airbnb.repository;

import com.airbnb.entity.AppUser;
import com.airbnb.entity.Property;
import com.airbnb.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("Select r from  Review r where r.property=:property and r.appUser=:appUser")
    Optional<Review> findByAppUserAndProperty(
          @Param("appUser")  AppUser appUser,
          @Param("property")   Property property
            );


    @Query("Select r from Review r where r.appUser=:user")
  List<Review> findReviewsByUser(
         @Param("user") AppUser appUser
  );

}
