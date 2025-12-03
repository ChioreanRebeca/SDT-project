package com.example.business_service.repository;

import com.example.business_service.model.BusinessProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BusinessRepository extends JpaRepository<BusinessProfile, Long> {
    List<BusinessProfile> findByNameContainingIgnoreCase(String name);
    List<BusinessProfile> findByCategoryContainingIgnoreCase(String category);
    List<BusinessProfile> findByAddressContainingIgnoreCase(String address);
}