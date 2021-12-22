package com.ernyka.cardmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LowCreditException extends RuntimeException {
    public LowCreditException(String msg) {
        super(msg, null);
    }
}
