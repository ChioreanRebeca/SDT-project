package com.example.review_service.controller;

import com.example.review_service.model.Review;
import com.example.review_service.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService service;

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return service.createReview(review);
    }

    @GetMapping("/business/{businessId}")
    public List<Review> getReviews(@PathVariable Long businessId) {
        return service.getReviewsForBusiness(businessId);
    }
}
