package com.example.notification_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping //handles http post requests sent to /api/notifications
    public void receiveNotification(@RequestBody String message) {
        // Simulating sending an email/SMS
        log.info("------------------------------------------------");
        log.info("🔔 NOTIFICATION RECEIVED: {}", message);
        log.info("------------------------------------------------");
    }
}
