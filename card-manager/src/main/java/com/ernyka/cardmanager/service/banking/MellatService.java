package com.ernyka.cardmanager.service.banking;

import com.ernyka.cardmanager.viewModel.CardTransactionViewModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class MellatService implements MoneyTransfer{
    private String message;

    @Override
    public MoneyTransfer transferMoney() {
        message = "Mellat success";
        System.out.println(message);
        return new MellatService(message);
    }
}
