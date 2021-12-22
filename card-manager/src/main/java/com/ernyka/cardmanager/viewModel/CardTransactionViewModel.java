package com.ernyka.cardmanager.viewModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardTransactionViewModel {
    private String cardFromUuid;
    private String cardToUuid;
    private Long amount;
    private Integer cvv2;
    private String secondPass;
}
