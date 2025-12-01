package com.example.business_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class BusinessProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private String address;
    private String website;

    // Private constructor for Builder
    private BusinessProfile(Builder builder) {
        this.name = builder.name;
        this.category = builder.category;
        this.address = builder.address;
        this.website = builder.website;
    }

    public static class Builder {
        private String name;
        private String category;
        private String address;
        private String website;

        public Builder(String name) { this.name = name; }
        public Builder withCategory(String category) { this.category = category; return this; }
        public Builder withAddress(String address) { this.address = address; return this; }
        public Builder withWebsite(String website) { this.website = website; return this; }
        public BusinessProfile build() { return new BusinessProfile(this); }
    }
}