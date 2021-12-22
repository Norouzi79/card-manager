package com.ernyka.cardmanager.service;

import com.ernyka.cardmanager.configuration.MessageConfiguration;
import com.ernyka.cardmanager.dto.CardDTO;
import com.ernyka.cardmanager.model.Card;
import com.ernyka.cardmanager.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CardService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final CardRepository repository;
    private final RabbitTemplate template;

    public Page<CardDTO> getAll(Integer page, Integer size) {
        return repository.getAllByUuidNotNullAndIsDeletedFalse(PageRequest.of(page, size, Sort.by("id")));
    }

    public CardDTO getCardDTOByUuid(String uuid) {
        return repository.getByUuidEquals(uuid);
    }

    @Transactional
    public String save(Card card) {
        Random rand = new Random();
        if (!card.getOwnerFirstName().isEmpty() && !card.getOwnerLastName().isEmpty()) {
            if (!card.getSecondPass().isEmpty()) {
                card.setSecondPass(encryptPassword(card.getSecondPass()));
            }
            Integer twoDigitInt = rand.nextInt(99);
            Long multiply = 0L;
            if (twoDigitInt < 10) {
                multiply = twoDigitInt * 10000000000000L;
            } else {
                multiply = twoDigitInt * 1000000000000L;
            }
            multiply += 6100000000000000L;
            String uuid = String.format("%16d", (rand.nextLong() % 1000000000000L) + multiply);
            card.setUuid(uuid);
            Card saved = repository.save(card);
            CompletableFuture.runAsync(() ->
                    template.convertAndSend(MessageConfiguration.EXCHANGE, MessageConfiguration.CARD_ROUTING, "card saved, uuid: " + saved.getUuid()));
            return saved.getUuid();
        } else {
            CompletableFuture.runAsync(() ->
                    template.convertAndSend(MessageConfiguration.EXCHANGE, MessageConfiguration.CARD_ROUTING, "card saving failed!"));
            return null;
        }
    }

    @Transactional
    protected void update(Card... card) {
        for (Card item : card) {
            if (item.getId() != null) {
                repository.save(item);
            }
        }
    }

    @Transactional
    public void deleteByUuid(String uuid) {
        Card card = getCardByUuid(uuid);
        if (card != null) {
            card.setIsDeleted(true);
            repository.save(card);
            CompletableFuture.runAsync(() ->
                    template.convertAndSend(MessageConfiguration.EXCHANGE, MessageConfiguration.CARD_ROUTING, "card: " + uuid + " deleted"));
        } else {
            CompletableFuture.runAsync(() ->
                    template.convertAndSend(MessageConfiguration.EXCHANGE, MessageConfiguration.CARD_ROUTING, "کارتی با کد ارائه شده یافت نشد!"));
        }
    }

    protected Card getCardByUuid(String uuid) {
        return Optional.ofNullable(uuid).map(repository::getByUuidEqualsAndIsDeletedFalse).orElse(null);
    }

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
