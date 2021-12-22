package com.ernyka.cardmanager.dto;

import java.io.Serializable;
import java.util.Date;

public interface CardDTO extends Serializable {
    public Long getId();

    public Long getCredit();

    public Date getExpirationDate();

    public String getUuid();
}
