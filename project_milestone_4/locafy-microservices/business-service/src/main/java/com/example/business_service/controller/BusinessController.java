package com.example.business_service.controller;

import com.example.business_service.model.BusinessProfile;
import com.example.business_service.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import lombok.Data;


@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    @Autowired
    private BusinessService service;

    @PostMapping
    public BusinessProfile createBusiness(@RequestBody BusinessProfileDTO dto) {
        // Using Builder Pattern
        BusinessProfile profile = new BusinessProfile.Builder(dto.getName())
                .withCategory(dto.getCategory())
                .withAddress(dto.getAddress())
                .withWebsite(dto.getWebsite())
                .build();
        return service.save(profile);
    }

    @GetMapping("/{id}")
    public BusinessProfile getBusiness(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/search")
    public List<BusinessProfile> search(@RequestParam String query, @RequestParam(defaultValue = "name") String type) {
        return service.search(query, type);
    }

    // DTO Helper Class
    @Data
    static class BusinessProfileDTO {
        private String name;
        private String category;
        private String address;
        private String website;
    }
}
