package com.example.business_service.strategy;

import com.example.business_service.model.BusinessProfile;
import com.example.business_service.repository.BusinessRepository;

import java.util.List;

// Strategy 2: Search by Category
public class CategorySearchStrategy implements SearchStrategy {
    @Override
    public List<BusinessProfile> search(String query, BusinessRepository repository) {
        return repository.findByCategoryContainingIgnoreCase(query);
    }
}
