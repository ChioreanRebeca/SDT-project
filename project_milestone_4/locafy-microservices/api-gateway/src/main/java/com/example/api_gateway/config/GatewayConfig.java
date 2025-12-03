package com.example.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // ---------------------------------------------------
                // ROUTE 1: Business Service
                // ---------------------------------------------------
                .route("business-service", r -> r.path("/api/businesses/**")
                        // OPTION A: Use this for local debugging if Eureka fails
                        // .uri("http://localhost:8081")

                        // OPTION B: Use this for Production (Requires Eureka + LoadBalancer)
                        .uri("lb://BUSINESS-SERVICE")
                )

                // ---------------------------------------------------
                // ROUTE 2: Review Service
                // ---------------------------------------------------
                .route("review-service", r -> r.path("/api/reviews/**")
                        .uri("lb://REVIEW-SERVICE")
                )

                // ---------------------------------------------------
                // ROUTE 3: Notification Service
                // ---------------------------------------------------
                .route("notification-service", r -> r.path("/api/notifications/**")
                        .uri("lb://NOTIFICATION-SERVICE")
                )

                .build();
    }
}