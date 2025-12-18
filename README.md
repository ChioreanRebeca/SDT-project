# Locafy — Local Business Discovery & Listings

Locafy is a **local business discovery platform** implemented using **Spring Boot Microservices**.

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


# Microservices Architecture

## Architecture

1.  **Discovery Server (Eureka):** Service Registry.
2.  **API Gateway:** Routes all traffic (Port 8080).
3.  **Business Service:** Manages business profiles (Uses Builder & Strategy patterns).
4.  **Review Service:** Manages reviews. Validates business existence via REST calls to Business Service.
5.  **Notification Service:** Simulates sending alerts when reviews are posted.

## Prerequisites

* Docker & Docker Compose
* Java 17 / Maven (for building the JARs)

## How to Run

### Step 1: Build the Microservices Jar's
Navigate to the directory of each microservice and run Maven to build it. 

```bash
# example for packaging all Maven packages(one by one)
cd discovery-server && mvn clean package -DskipTests
cd api-gateway && mvn clean package -DskipTests 
cd business-service && mvn clean package -DskipTests 
cd review-service && mvn clean package -DskipTests
cd notification-service && mvn clean package -DskipTests 
```
### Step 2: Build the Containers
Navigate to the root directory and run docker to build all service containers. 
```bash
# example for building all docker containers
cd locafy-microservices
docker compose up --build
```
### Step 3: Look at the Eureka Discovery server to check if all services are runnig
click here[http://localhost:8761/]

### Step 4: Run Postman Tests
Run the postman tests. The test file is saved under the name 'locafy-test.json'


# Microservices Architecture with RabbitMQ implementation
---

## Architecture Overview

- **Discovery Server (Eureka)** – Service Registry
- **API Gateway** – Routes all traffic (**Port 8080**)
- **Business Service** – Manages business profiles (uses **Builder** & **Strategy** patterns)
- **Review Service** – Manages reviews; validates business existence via REST calls to Business Service
- **Notification Service** – Sends alerts when reviews are posted
- **RabbitMQ Broker** – Handles asynchronous messaging between services

---

## Part 1: Architecture Upgrade (Synchronous vs. Asynchronous)

### The Shift to Async Messaging

Critical inter-service communication was transitioned from **synchronous HTTP** to **asynchronous messaging** using **RabbitMQ**.

### Comparison

| Feature | Old Architecture (Synchronous) | New Architecture (Asynchronous) |
|------|------------------------------|--------------------------------|
| Communication | Review Service calls Notification Service directly via Feign Client (HTTP) | Review Service publishes an event to RabbitMQ |
| Dependency | High coupling; if Notification Service is down, the request fails or hangs | Loose coupling; Review Service returns immediately |
| Fault Tolerance | Low; failures propagate to users | High; messages persist until consumer recovers |
| Performance | User waits for review save **and** email sending | User waits only for review save; notification is background |

---

## Implementation Details

### 1. The Broker (RabbitMQ)

Added to `docker-compose.yml` to act as the message bus:

```yaml
rabbitmq:
  image: rabbitmq:3.12-management
  ports:
    - "5672:5672"   # App communication port
    - "15672:15672" # Management Dashboard
```

---

### 2. The Producer (Review Service)

**Configuration**
- Defines `notification.queue`
- Defines `review.exchange`
- Defines a routing key

**Logic**
- Replaces direct `NotificationClient` calls
- Injects `RabbitTemplate`

```java
// Async Fire-and-Forget
rabbitTemplate.convertAndSend(
    RabbitMQConfig.EXCHANGE,
    RabbitMQConfig.ROUTING_KEY,
    message
);
```

---

### 3. The Consumer (Notification Service)

- **Listener**: Uses `@RabbitListener` to watch `notification.queue`
- **Action**: Automatically consumes messages and logs them (simulating email sending)

---

## Part 2: CI/CD Pipeline (GitHub Actions)

A complete CI/CD pipeline is defined in:

```
.github/workflows/maven-docker.yml
```

The pipeline automatically **builds, tests, and deploys** the system whenever code is pushed to the `main` branch.

---

### Pipeline Workflow

1. **Checkout Code**  
   Pulls the latest code from the repository

2. **Setup Java 17**  
   Installs Eclipse Temurin JDK

3. **Build & Test**  
   Runs:
   ```bash
   mvn clean package
   ```
   - Compiles each microservice individually
   - Runs all JUnit tests
   - Pipeline stops on test failure

4. **Dockerize**  
   Builds Docker images using:
   ```bash
   docker compose build
   ```

5. **Deployment Smoke Test**  
   - Spins up the entire architecture (5 services + RabbitMQ)
   - Waits **30 seconds** for startup
   - Verifies containers are running (`docker ps`)
   - Checks logs for successful boot

---

### How to Monitor

- Go to the **Actions** tab in the GitHub repository
- **Green checkmark ✅** indicates a successful pipeline run

---

## How to Run Locally

### Prerequisites

- Docker Desktop (with **WSL 2** on Windows)
- Java 17
- Maven

---

### Build the JARs

Before running Docker:

```bash
mvn clean package -DskipTests
```

---

### Start and Build the Containers

```bash
docker compose up --build
```

---

### Tools for verification

- **Eureka Dashboard**  
  http://localhost:8761  
  *(All services should be UP)*

- **RabbitMQ Dashboard**  
  http://localhost:15672  
  **User/Pass:** `guest / guest`

- **API Gateway**  
  http://localhost:8080/api/businesses/1

---

### Testing with Postman

1. Import the provided **Postman Collection** from the github repo
2. Run automated tests
3. **Create Review (Test 3)** – Sends a `POST` request via API Gateway
4. **Verify Async Flow** – Check logs of `notification-service`

**Expected Log Output:**
```
🐰 RABBITMQ MESSAGE RECEIVED: New Review Posted...
```



  
## Team members
- Ana Adăscăliței  1241EA sgr1
- Naomi Liță       1241EA sgr2
- Rebeca Chiorean  1241EA sgr2
