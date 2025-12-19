package com.example.review_service.controller;

import com.example.review_service.model.Review;
import com.example.review_service.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//REST Controller
@RestController
@RequestMapping("/api/reviews")  //any incomming http that starts with /api/reviews should be sent to this class.
public class ReviewController {
    @Autowired  //this literally lets spring do this private ReviewService service = new ReviewService(); but smarter
    private ReviewService service;

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return service.createReview(review);
    }

    @GetMapping("/business/{businessId}") // /api/reviews/business/{businessId} returns all reviews for the business
    public List<Review> getReviews(@PathVariable Long businessId) {
        return service.getReviewsForBusiness(businessId);
    }
}
