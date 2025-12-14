package com.example.business_service.service;

import com.example.business_service.model.BusinessProfile;
import com.example.business_service.repository.BusinessRepository;
import com.example.business_service.strategy.*; // Assumes strategies are public or in same package
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusinessService {
    @Autowired
    private BusinessRepository repository;

    // Logic for Strategy Selection
    public List<BusinessProfile> search(String query, String type) {
        SearchStrategy strategy;

        switch (type.toLowerCase()) {
            case "category":
                strategy = new CategorySearchStrategy();
                break;
            case "location":
                strategy = new LocationSearchStrategy();
                break;
            case "name":
            default:
                strategy = new NameSearchStrategy();
                break;
        }

        return strategy.search(query, repository);
    }

    public BusinessProfile save(BusinessProfile profile) {
        return repository.save(profile);
    }

    public BusinessProfile findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found"));
    }

}