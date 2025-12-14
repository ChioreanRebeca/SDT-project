# Locafy - Microservices Architecture - EXTENDED

Locafy is a local business discovery platform implemented using Spring Boot Microservices.

## Architecture

1.  **Discovery Server (Eureka):** Service Registry.
2.  **API Gateway:** Routes all traffic (Port 8080).
3.  **Business Service:** Manages business profiles (Uses Builder & Strategy patterns).
4.  **Review Service:** Manages reviews. Validates business existence via REST calls to Business Service.
5.  **Notification Service:** Simulates sending alerts when reviews are posted.


Part 1: Architecture Upgrade (Synchronous vs. Asynchronous)
Current State (Synchronous): Review Service -> calls Notification Service directly via HTTP (Feign).

Problem: If the Notification Service is down, the Review Service fails or hangs. The user has to wait for the email to be sent before their review is saved.

New State (Asynchronous with RabbitMQ): Review Service -> sends a message to RabbitMQ -> Notification Service picks it up later.

Benefit: Decoupling. The Review Service doesn't care if the Notification Service is online. It saves the review, sends a message to the queue, and responds to the user immediately (Scalability & Fault Tolerance).


The Change
services:
  # ... (Discovery, Gateway, Business Service remain the same) ...

  # NEW: Message Broker
  rabbitmq:
    image: rabbitmq:3.12-management
    container_name: rabbitmq
    ports:
      - "5672:5672"   # App communication port
      - "15672:15672" # Management Dashboard (http://localhost:15672)
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest

  review-service:
    build: ./review-service
    container_name: review-service
    ports:
      - "8082:8082"
    environment:
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://discovery-server:8761/eureka/
      - SPRING_RABBITMQ_HOST=rabbitmq # Connect to Rabbit container
    depends_on:
      - discovery-server
      - business-service
      - rabbitmq # Wait for RabbitMQ


adding this to the review-service pom: https://spring.io/projects/spring-amqp
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

adding this to: src/main/java/com/locafy/review/config/RabbitMQConfig.java

package com.locafy.review.config;

    import org.springframework.amqp.core.*;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class RabbitMQConfig {
        public static final String QUEUE = "notification.queue";
        public static final String EXCHANGE = "review.exchange";
        public static final String ROUTING_KEY = "review.routingKey";

        @Bean
        public Queue queue() { return new Queue(QUEUE); }

        @Bean
        public TopicExchange exchange() { return new TopicExchange(EXCHANGE); }

        @Bean
        public Binding binding(Queue queue, TopicExchange exchange) {
            return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
        }
    }

and then 
Update Service Logic: Modify ReviewService.java to use RabbitTemplate instead of NotificationClient.





old code to be added back
package com.example.review_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ReviewServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReviewServiceApplication.class, args);
    }
}



Use this for Docker and not the intelij plugin:  docker compose up --build



# 1. Ensure fresh JARs are available
mvn clean package -DskipTests

# 2. Build and start containers using the V2 plugin
docker compose up --build


Step 4: CI/CD Pipeline (GitHub Actions)
This pipeline will run every time you push code to GitHub. It will verify that your Java code compiles and that your Docker containers can build and start up successfully.

Create this file structure in your project root: .github/workflows/maven-docker.yml