package com.example.review_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static String QUEUE = "notification.queue";
    public static String EXCHANGE = "review.exchange";
    public static String ROUTING_KEY = "review.routingKey";

    @Bean
    public Queue queue() { return new Queue(QUEUE); }  //this is not the queue of java.util.queue but
                                                        //but it is org.springframework.amqp.core.Queue;

    @Bean
    public TopicExchange exchange() { return new TopicExchange(EXCHANGE); }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
