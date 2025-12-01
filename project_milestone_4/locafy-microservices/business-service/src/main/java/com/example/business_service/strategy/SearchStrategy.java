package com.example.business_service.strategy;

import com.example.business_service.model.BusinessProfile;
import com.example.business_service.repository.BusinessRepository;
import java.util.List;

// Interface
public interface SearchStrategy {
    List<BusinessProfile> search(String query, BusinessRepository repository);
}

