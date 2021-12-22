package com.ernyka.cardmanager.service.banking;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class OthersService implements MoneyTransfer {
    private String message;

    @Override
    public MoneyTransfer transferMoney() {
        message = "Other success";
        System.out.println(message);
        return new OthersService(message);
    }
}
