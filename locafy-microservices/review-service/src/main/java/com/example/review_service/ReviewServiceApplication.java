package com.example.review_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

//@SpringBootApplication
//@EnableDiscoveryClient
//@EnableFeignClients
//@EnableScheduling // <--- CRITICAL: Enables the timer
//public class ReviewServiceApplication {
//
//    private static final Logger log =
//            LoggerFactory.getLogger(ReviewServiceApplication.class);
//
//    public static void main(String[] args) {
//        SpringApplication.run(ReviewServiceApplication.class, args);
//        log.info("🚀 APPLICATION STARTED");
//    }
//
//    @Scheduled(fixedRate = 3000)
//    public void heartbeat() {
//        log.info("💓 HEARTBEAT – app is alive");
//    }
//}

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ReviewServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReviewServiceApplication.class, args);
    }
}