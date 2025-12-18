# Locafy – Microservices Architecture (Extended)

Locafy is a **local business discovery platform** implemented using **Spring Boot Microservices**.

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

