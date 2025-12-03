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
