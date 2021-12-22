package com.ernyka.cardmanager.service.banking;

import com.ernyka.cardmanager.viewModel.CardTransactionViewModel;

public class MoneyTransferFactory {
    public static MoneyTransfer transfer(CardTransactionViewModel model) {
        if(model.getCardFromUuid().startsWith("6104")) {
            return new MellatService();
        } else {
            return new OthersService();
        }
    }
}
