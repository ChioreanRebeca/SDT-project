# Locafy — Local Business Discovery & Listings

## Problem Statement

The objective of Locafy, a web platform, is to help local businesses register and present their services, to enable residents to discover, review, and to contact those businesses.
The system must support distinct user roles (business owners, customers, administrators), provide searchable, image-rich business profiles, and ensure secure, maintainable deployment (Spring Boot backend, Spring Security, Docker). 
The project will evaluate three architectural approaches — monolithic, containerized (modular), and microservices — and compare them based on deployment complexity, scalability, data consistency, and operational cost.

## Functionalities:

1. Registration & Login:
   - Implement user registration and authentication functionalities.
   - Ensure secure storage of user data and sensitive information.
   - Provide a seamless and user-friendly registration process.
   - Supports three distinct roles: Business Owners, Customers, and Administrators.
   - Role-based access is securely managed using Spring Security.

2. Business Listing Management
   - Business owners can create and manage business profiles.
   - Upload storefront and service-related images/ contact information/ location/ business website details

3. Intelligent Business Discovery  
   Customers can:
   - Search, filter, and explore local businesses by name, category, or location.
   - Explore interactive maps (via Google Maps API) and business pages.
   - Read and post reviews.
     
5. Interaction and Feedback    
   Customers can:
   - Mark favorites for quick access.
   - Post reviews and ratings.
     
   - Business owners receive notifications for new reviews or messages.
   - Admins moderate inappropriate content.

## Software design patterns used
__1. Behavioral: Strategy Pattern__
   
   __Used for:__ Dynamic business search and filtering.  
   __Problem:__ The search behavior varies (by name, category, location, or rating), and adding new types of searches should not require modifying existing code.  
   __Solution:__ Encapsulate search logic into interchangeable strategies. New search types can be added without altering existing code (open/closed principle). Improves flexibility and testability.
   ```
     public interface SearchStrategy {
          List<Business> search(String query);
      }
      
      public class NameSearchStrategy implements SearchStrategy { ... }
      public class CategorySearchStrategy implements SearchStrategy { ... }
      public class LocationSearchStrategy implements SearchStrategy { ... }
   ```

__2. Behavioral: Observer Pattern__

   __Used for:__ Event-driven notifications (e.g., new review, new message, new business registration).  
   __Problem:__ When a review is created, multiple components (notifications, analytics, reputation scoring) should react automatically without tight coupling.  
   __Solution:__ Implement an event-publishing mechanism that promotes loose coupling. Components can subscribe/unsubscribe without modifying others.
   ```
   EventBus.publish(new ReviewCreatedEvent(review));
   ```

__3. Creational: Singleton Pattern__  

   __Used for:__ Shared resources like cache manager, email service, and rate limiter.  
   __Problem:__ Certain resources (e.g., cache, email sender) should have only one global instance to ensure consistency and reduce overhead.  
   __Solution:__ Use Singleton components, handled automatically by Spring’s @Service or @Component annotations this ensures centralized control, prevents redundant instantiations, and optimizes memory usage.
     
__4. Creational: Builder Pattern__  

   __Used for:__ Constructing complex business profiles or user objects.  
   __Problem:__ A business profile contains many optional fields (e.g., phone, website, images, hours), making constructors unreadable and error-prone.  
   __Solution:__ Use the Builder pattern to construct such objects step by step. This improves code readability and prevents constructor overloading. Also, it supports immutable objects and easier maintenance.
   ```
   BusinessProfile profile = new BusinessProfile.Builder("BusinessName")
    .withCategory("Restaurant")
    .withWebsite("www.example.com")
    .withAddress("Main Street 10")
    .build();

   ```
__5. Structural: Adapter Pattern__  

   __Used for:__ Integration with external APIs (Google Maps, Email services).  
   __Problem:__ External APIs may have incompatible interfaces, and their implementation details might change over time.  
   __Solution:__ Create an adapter layer between the internal application logic and external APIs. TFor example if the vendor API changes, only the adapter needs to be updated.
   ```
      public interface MapService {
    Coordinates getCoordinates(String address);
}

public class GoogleMapsAdapter implements MapService {
    private GoogleMapsApi api;
    public Coordinates getCoordinates(String address) { return api.lookup(address); }
}

   ```

  
## Team members
- Ana Adăscăliței  1241EA sgr1
- Naomi Liță       1241EA sgr2
- Rebeca Chiorean  1241EA sgr2
