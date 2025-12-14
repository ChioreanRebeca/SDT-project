package com.example.business_service.loader;


import com.example.business_service.model.BusinessProfile;
import com.example.business_service.repository.BusinessRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final BusinessRepository repository;

    public DataLoader(BusinessRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists to prevent duplication on restarts if using persistent DB
        if (repository.count() == 0) {

            // Using the Builder Pattern we implemented
            BusinessProfile defaultBusiness = new BusinessProfile.Builder("Locafy HQ - The Default Coffee Shop")
                    .withCategory("Cafe")
                    .withAddress("123 Startup Avenue, Tech City")
                    .withWebsite("www.locafy-demo.com")
                    .build();

            repository.save(defaultBusiness);

            System.out.println("✅ Default Business Loaded: " + defaultBusiness.getName());
        }
    }
}