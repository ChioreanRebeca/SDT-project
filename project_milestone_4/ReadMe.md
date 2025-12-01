# Locafy - Microservices Architecture

Locafy is a local business discovery platform implemented using Spring Boot Microservices.

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

### Step 1: Build the Microservices
Navigate to the root directory and run Maven to build all services. 
*(If you are in a terminal and have a parent pom, otherwise build individually)*:

```bash
# Example if building individually
cd discovery-server && mvn clean package -DskipTests && cd ..
cd api-gateway && mvn clean package -DskipTests && cd ..
cd business-service && mvn clean package -DskipTests && cd ..
cd review-service && mvn clean package -DskipTests && cd ..
cd notification-service && mvn clean package -DskipTests && cd ..


!!!! Change the text in here./ Information is outdated !!!!!