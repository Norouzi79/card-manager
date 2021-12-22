package com.ernyka.notificationservice.service;

import com.ernyka.notificationservice.config.ManagerConfiguration;
import com.ernyka.notificationservice.config.MessageConfiguration;
import com.ernyka.notificationservice.dto.CardTransactionDTO;
import com.ernyka.notificationservice.dto.CardTransactionMessage;
import com.ernyka.notificationservice.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final RabbitTemplate template;

    @RabbitListener(queues = ManagerConfiguration.RECEIVING_TRANSACTION_QUEUE_NAME)
    public void getTransaction(CardTransactionDTO transactionDTO) {
        CardTransactionMessage transactionMessage = new CardTransactionMessage()
                .setAmount(transactionDTO.getAmount())
                .setStatus(transactionDTO.getStatus().message)
                .setCreatedDate(transactionDTO.getCreatedDate());
        if (transactionDTO.getStatus().equals(TransactionStatus.Success) ||
                transactionDTO.getStatus().equals(TransactionStatus.Failed_Due_To_Low_Credit) ||
                transactionDTO.getStatus().equals(TransactionStatus.Failed_Due_To_Wrong_SecondPass)) {
            transactionMessage.setCardFromUuid(transactionDTO.getCardFrom().getUuid());
            transactionMessage.setCardToUuid(transactionDTO.getCardTo().getUuid());
        } else {
            transactionMessage.setFailedFromUuid(transactionDTO.getFailedFromUuid());
            transactionMessage.setFailedToUuid(transactionDTO.getFailedToUuid());
        }
        template.convertAndSend(MessageConfiguration.EXCHANGE, MessageConfiguration.ROUTING, transactionMessage);
    }

    @RabbitListener(queues = ManagerConfiguration.RECEIVING_CARD_QUEUE_NAME)
    public void getCard(String message) {
        template.convertAndSend(MessageConfiguration.EXCHANGE, MessageConfiguration.ROUTING, message);
    }
}
