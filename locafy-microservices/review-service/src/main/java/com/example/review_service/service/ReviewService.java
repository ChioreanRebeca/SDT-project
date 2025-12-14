package com.example.review_service.service;


import com.example.review_service.client.BusinessClient;
import com.example.review_service.client.NotificationClient;
import com.example.review_service.config.RabbitMQConfig;
import com.example.review_service.model.Review;
import com.example.review_service.repository.ReviewRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository repository;

    @Autowired
    private BusinessClient businessClient;

    // REPLACED: NotificationClient (Synchronous/Feign) with RabbitTemplate (Async/AMQP)
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Review createReview(Review review) {
        // 1. Inter-service Validation (Still Synchronous via Feign)
        // We still need to confirm the business exists before saving the review
        try {
            businessClient.getBusiness(review.getBusinessId());
        } catch (Exception e) {
            throw new RuntimeException("Invalid Business ID: " + review.getBusinessId());
        }

        // 2. Persist Review
        Review saved = repository.save(review);

        // 3. Trigger Notification (Asynchronous Event)
        // Instead of calling the API directly, we drop a message into the exchange
        String message = "New Review Posted for Business ID: " + review.getBusinessId();

        // Use constants from your RabbitMQConfig class
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                message
        );

        return saved;
    }

    public List<Review> getReviewsForBusiness(Long businessId) {
        return repository.findByBusinessId(businessId);
    }
}
