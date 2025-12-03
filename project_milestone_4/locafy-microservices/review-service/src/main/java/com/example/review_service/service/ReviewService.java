package com.example.review_service.service;


import com.example.review_service.client.BusinessClient;
import com.example.review_service.client.NotificationClient;
import com.example.review_service.model.Review;
import com.example.review_service.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository repository;
    @Autowired private BusinessClient businessClient;
    @Autowired private NotificationClient notificationClient;

    public Review createReview(Review review) {
        // 1. Inter-service Validation
        try {
            businessClient.getBusiness(review.getBusinessId());
        } catch (Exception e) {
            throw new RuntimeException("Invalid Business ID: " + review.getBusinessId());
        }

        // 2. Persist Review
        Review saved = repository.save(review);

        // 3. Trigger Notification (Simulating Event)
        notificationClient.sendNotification("New Review Posted for Business ID: " + review.getBusinessId());

        return saved;
    }

    public List<Review> getReviewsForBusiness(Long businessId) {
        return repository.findByBusinessId(businessId);
    }
}
