package com.example.notification_service.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    // Listens to the queue defined in the Review Service
    @RabbitListener(queues = "notification.queue")
    public void consumeMessage(String message) {
        log.info("------------------------------------------------");
        log.info("🐰 RABBITMQ MESSAGE RECEIVED: {}", message);
        log.info("------------------------------------------------");
        // Here we would add logic to send actual Email/SMS
    }
}
