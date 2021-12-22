package com.ernyka.notificationservice.enums;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum TransactionStatus {
    Failed_Due_To_Amount_Null("ناموفق به علت عدم ورود مبلغ تراکنش"),
    Failed_Due_To_CardFrom_Null_Or_Wrong("ناموفق به علت خالی بودن شماره کارت مبدا"),
    Failed_Due_To_CardFrom_NotFound("ناموفق به علت خالی بودن شماره کارت مقصد"),
    Failed_Due_To_CardTo_Null_Or_Wrong("ناموفق به علت اشتباه بودن شماره کارت مبدا"),
    Failed_Due_To_CardTo_NotFound("ناموفق به علت اشتباه بودن شماره کارت مقصد"),
    Failed_Due_To_Low_Credit("ناموفق به علت عدم موجودی کافی"),
    Failed_Due_To_Wrong_SecondPass("ناموفق به علت رمز دوم غلط"),
    Success("موفق");

    public String message;

    private TransactionStatus(String message) {
        this.message = message;
    }
}
