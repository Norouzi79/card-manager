package com.ernyka.cardmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NullException extends RuntimeException {
    public NullException(String msg) {
        super(msg, null);
    }
}
