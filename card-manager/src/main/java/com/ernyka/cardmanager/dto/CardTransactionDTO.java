package com.ernyka.cardmanager.dto;

import com.ernyka.cardmanager.model.TransactionStatus;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;

public interface CardTransactionDTO extends Serializable {
    @Nullable
    public CardDTO getCardFrom();

    @Nullable
    public CardDTO getCardTo();

    public Date getCreatedDate();

    public Long getAmount();

    public TransactionStatus getStatus();

    public String getFailedFromUuid();

    public String getFailedToUuid();

    public interface CardDTO extends Serializable {
        public String getUuid();
    }
}
