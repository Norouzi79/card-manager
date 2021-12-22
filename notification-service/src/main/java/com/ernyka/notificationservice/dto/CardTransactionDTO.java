package com.ernyka.notificationservice.dto;

import com.ernyka.notificationservice.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTransactionDTO implements Serializable {
    public Card cardFrom;

    public Card cardTo;

    public Date createdDate;

    public Long amount;

    public TransactionStatus status;

    public String failedFromUuid;

    public String failedToUuid;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Card implements Serializable {
        public String uuid;
    }
}
