package com.ernyka.cardmanager.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum TransactionStatus {
    Failed_Due_To_Amount_Null,
    Failed_Due_To_CardFrom_Null_Or_Wrong,
    Failed_Due_To_CardFrom_NotFound,
    Failed_Due_To_CardTo_Null_Or_Wrong,
    Failed_Due_To_CardTo_NotFound,
    Failed_Due_To_Low_Credit,
    Failed_Due_To_Wrong_SecondPass,
    Success
}
