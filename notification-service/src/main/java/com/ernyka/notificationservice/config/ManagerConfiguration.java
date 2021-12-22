package com.ernyka.notificationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManagerConfiguration {
    public static final String RECEIVING_CARD_QUEUE_NAME = "card-manager-card-queue";
    public static final String RECEIVING_TRANSACTION_QUEUE_NAME = "card-manager-transaction-queue";
    public static final String RECEIVING_EXCHANGE = "card-manager-exchange";
    public static final String RECEIVING_CARD_ROUTING = "card-manager-card-routing";
    public static final String RECEIVING_TRANSACTION_ROUTING = "card-manager-transaction-routing";

    @Bean
    public Queue cardQueue() {
        return new Queue(RECEIVING_CARD_QUEUE_NAME);
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue(RECEIVING_TRANSACTION_QUEUE_NAME);
    }

    @Bean
    public TopicExchange managerExchange() {
        return new TopicExchange(RECEIVING_EXCHANGE);
    }

    @Bean
    public Binding cardBinding(Queue cardQueue, @Qualifier("managerExchange") TopicExchange managerExchange) {
        return BindingBuilder.bind(cardQueue).to(managerExchange).with(RECEIVING_CARD_ROUTING);
    }

    @Bean
    public Binding transactionBinding(Queue transactionQueue, @Qualifier("managerExchange") TopicExchange managerExchange) {
        return BindingBuilder.bind(transactionQueue).to(managerExchange).with(RECEIVING_TRANSACTION_ROUTING);
    }
}
