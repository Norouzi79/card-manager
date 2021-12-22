package com.ernyka.cardmanager.configuration;


import com.ernyka.cardmanager.model.TransactionStatus;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfiguration {

    public static final String CARD_QUEUE_NAME = "card-manager-card-queue";
    public static final String TRANSACTION_QUEUE_NAME = "card-manager-transaction-queue";
    public static final String EXCHANGE = "card-manager-exchange";
    public static final String CARD_ROUTING = "card-manager-card-routing";
    public static final String TRANSACTION_ROUTING = "card-manager-transaction-routing";

    @Bean
    public Queue cardQueue() {
        return new Queue(CARD_QUEUE_NAME);
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue(TRANSACTION_QUEUE_NAME);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding cardBinding(Queue cardQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(cardQueue).to(topicExchange).with(CARD_ROUTING);
    }

    @Bean
    public Binding transactionBinding(Queue transactionQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(transactionQueue).to(topicExchange).with(TRANSACTION_ROUTING);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
