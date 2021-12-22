package com.ernyka.cardmanager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
public class CardTransaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "CARD_FROM")
    private Card cardFrom;

    @ManyToOne(optional = true)
    @JoinColumn(name = "CARD_TO")
    private Card cardTo;

    @Column
    private Date createdDate;

    @Column
    private Long amount;

    @Enumerated
    private TransactionStatus status;

    @Column
    private String failedFromUuid;
    @Column
    private String failedToUuid;

    public CardTransaction(Long cardFromId, Long cardToId, Long amount) {
        this.cardFrom = new Card();
        this.cardTo = new Card();
        this.createdDate = new Date(System.currentTimeMillis());
        this.cardFrom.setId(cardFromId);
        this.cardTo.setId(cardToId);
        this.amount = amount;
    }

    public CardTransaction(String cardFromId, String cardToUuid, Long amount, TransactionStatus status) {
        this.createdDate = new Date(System.currentTimeMillis());
        this.failedFromUuid = cardFromId;
        this.failedToUuid = cardToUuid;
        this.amount = amount;
        this.status = status;
    }

    protected CardTransaction() {
    }
}
