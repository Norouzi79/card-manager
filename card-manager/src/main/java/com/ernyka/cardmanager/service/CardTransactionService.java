package com.ernyka.cardmanager.service;

import com.ernyka.cardmanager.configuration.MessageConfiguration;
import com.ernyka.cardmanager.dto.CardTransactionDTO;
import com.ernyka.cardmanager.model.Card;
import com.ernyka.cardmanager.model.CardTransaction;
import com.ernyka.cardmanager.model.TransactionStatus;
import com.ernyka.cardmanager.repository.CardTransactionRepository;
import com.ernyka.cardmanager.service.banking.MoneyTransferFactory;
import com.ernyka.cardmanager.viewModel.CardTransactionViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CardTransactionService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final CardTransactionRepository repository;
    private final CardService cardService;
    private final RabbitTemplate template;

    @Transactional
    public CardTransactionDTO save(CardTransactionViewModel transaction) {
        return transaction(transaction);
    }

    public CardTransactionDTO transaction(CardTransactionViewModel transaction) {
        Long savedId = null;
        savedId = savedIdValidation(transaction);
        CardTransactionDTO transactionDTO = repository.getByIdIs(savedId);
        CompletableFuture.runAsync(() -> template.convertAndSend(MessageConfiguration.EXCHANGE, MessageConfiguration.TRANSACTION_ROUTING, transactionDTO));
        return transactionDTO;
    }

    private Long savedIdValidation(CardTransactionViewModel transaction) {
        Long savedId = null;
        if (transaction.getAmount() == null) {
            savedId = amountIsNull(transaction);
        }
        savedId = checkUuids(transaction);
        if (savedId == null) {
            Card cardFrom = cardService.getCardByUuid(transaction.getCardFromUuid());
            Card cardTo = cardService.getCardByUuid(transaction.getCardToUuid());
            savedId = checkCardIsNotNull(transaction, cardFrom, cardTo);
            savedId = credentials(transaction, savedId, cardFrom, cardTo);
        }
        return savedId;
    }

    private Long credentials(CardTransactionViewModel transaction, Long savedId, Card cardFrom, Card cardTo) {
        if (checkCredentials(transaction, savedId, cardFrom)) {
            if (cardFrom.getCredit() >= transaction.getAmount()) {
                return successfulTransaction(cardFrom, cardTo, transaction);
            } else {
                return lowCreditTransaction(cardFrom, cardTo, transaction);
            }
        } else
            return wrongSecondPass(cardFrom, cardTo, transaction);
    }

    private boolean checkCredentials(CardTransactionViewModel transaction, Long savedId, Card cardFrom) {
        return savedId == null &&
                passwordEncoder.matches(transaction.getSecondPass(), cardFrom.getSecondPass()) &&
                cardFrom.getExpirationDate().after(new Date(System.currentTimeMillis()));
    }

    private Long checkCardIsNotNull(CardTransactionViewModel transaction, Card cardFrom, Card cardTo) {
        if (cardFrom == null) {
            return cardFromIsNull(transaction);
        } else if (cardTo == null) {
            return cardToIsNull(transaction);
        }
        return null;
    }

    private Long amountIsNull(CardTransactionViewModel transaction) {
        CardTransaction temp;
        temp = new CardTransaction(transaction.getCardFromUuid(),
                transaction.getCardToUuid(),
                transaction.getAmount(),
                TransactionStatus.Failed_Due_To_Amount_Null);
        return repository.save(temp).getId();
    }

    private Long cardFromIsNull(CardTransactionViewModel transaction) {
        CardTransaction temp;
        temp = new CardTransaction(transaction.getCardFromUuid(),
                transaction.getCardToUuid(),
                transaction.getAmount(),
                TransactionStatus.Failed_Due_To_CardFrom_NotFound);
        return repository.save(temp).getId();
    }

    private Long cardToIsNull(CardTransactionViewModel transaction) {
        CardTransaction temp;
        temp = new CardTransaction(transaction.getCardFromUuid(),
                transaction.getCardToUuid(),
                transaction.getAmount(),
                TransactionStatus.Failed_Due_To_CardTo_NotFound);
        return repository.save(temp).getId();
    }

    private Long checkUuids(CardTransactionViewModel transaction) {
        CardTransaction temp = null;
        String fromUuid = Optional.ofNullable(transaction)
                .map(CardTransactionViewModel::getCardFromUuid)
                .orElse(null);
        if (fromUuid == null) {
            temp = new CardTransaction(null, transaction.getCardToUuid(), transaction.getAmount(),
                    TransactionStatus.Failed_Due_To_CardFrom_Null_Or_Wrong);
        }
        String toUuid = Optional.ofNullable(transaction)
                .map(CardTransactionViewModel::getCardToUuid)
                .orElse(null);
        if (toUuid == null) {
            temp = new CardTransaction(transaction.getCardFromUuid(), transaction.getCardToUuid(), transaction.getAmount(),
                    TransactionStatus.Failed_Due_To_CardTo_Null_Or_Wrong);
        }
        return (fromUuid != null && !fromUuid.isEmpty() && toUuid != null && !toUuid.isEmpty()) ? null : repository.save(temp).getId();
    }

    private Long successfulTransaction(Card cardFrom, Card cardTo, CardTransactionViewModel transaction) {
        CardTransaction temp = null;
        System.out.println(MoneyTransferFactory.transfer(transaction).transferMoney());
        cardFrom.setCredit(cardFrom.getCredit() - transaction.getAmount());
        cardTo.setCredit(cardTo.getCredit() + transaction.getAmount());
        cardService.update(cardFrom, cardTo);
        temp = new CardTransaction(cardFrom.getId(), cardTo.getId(), transaction.getAmount());
        temp.setStatus(TransactionStatus.Success);
        return repository.save(temp).getId();
    }

    private Long lowCreditTransaction(Card cardFrom, Card cardTo, CardTransactionViewModel transaction) {
        CardTransaction temp = null;
        temp = new CardTransaction(cardFrom.getId(), cardTo.getId(), transaction.getAmount());
        temp.setStatus(TransactionStatus.Failed_Due_To_Low_Credit);
        return repository.save(temp).getId();
    }

    private Long wrongSecondPass(Card cardFrom, Card cardTo, CardTransactionViewModel transaction) {
        CardTransaction temp = null;
        temp = new CardTransaction(cardFrom.getId(), cardTo.getId(), transaction.getAmount());
        temp.setStatus(TransactionStatus.Failed_Due_To_Wrong_SecondPass);
        return repository.save(temp).getId();
    }

    public Page<CardTransactionDTO> getAllByCardFromUuid(String fromUuid, Integer page, Integer size) {
        return repository.getAllByCardFrom_Uuid(fromUuid, PageRequest.of(page, size, Sort.by("id")));
    }

    public Page<CardTransactionDTO> getAllByCardToUuid(String toUuid, Integer page, Integer size) {
        return repository.getAllByCardTo_Uuid(toUuid, PageRequest.of(page, size, Sort.by("id")));
    }

    public Page<CardTransactionDTO> getAllByCardFromUuidAndCardToUuid(String fromUuid, String toUuid, Integer page, Integer size) {
        return repository.getAllByCardFrom_UuidAndCardTo_Uuid(fromUuid, toUuid, PageRequest.of(page, size, Sort.by("id")));
    }

    public Page<CardTransactionDTO> getAllByStatusSuccess(Integer page, Integer size) {
        return repository.getAllByStatusIs(TransactionStatus.Success, PageRequest.of(page, size, Sort.by("id")));
    }

    public Page<CardTransactionDTO> getAllByStatusNotSuccess(Integer page, Integer size) {
        return repository.getAllByStatusNotAndStatusNotNull(TransactionStatus.Success, PageRequest.of(page, size, Sort.by("id")));
    }

    public Page<CardTransactionDTO> getAllByCreatedDateBetween(Date from, Date to, Integer page, Integer size) {
        return repository.getAllByCreatedDateBetween(from, to, PageRequest.of(page, size, Sort.by("id")));
    }

    public Page<CardTransactionDTO> getAllByCreatedDateBefore(Date date, Integer page, Integer size) {
        return repository.getAllByCreatedDateBefore(date, PageRequest.of(page, size, Sort.by("id")));
    }

    public Page<CardTransactionDTO> getAllByCreatedDateAfter(Date date, Integer page, Integer size) {
        return repository.getAllByCreatedDateAfter(date, PageRequest.of(page, size, Sort.by("id")));
    }
}
