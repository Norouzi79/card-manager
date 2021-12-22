package com.ernyka.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CardTransactionMessage {
    private String cardFromUuid;
    private String cardToUuid;
    private Date createdDate;
    private Long amount;
    private String status;
    private String failedFromUuid;
    private String failedToUuid;
}
