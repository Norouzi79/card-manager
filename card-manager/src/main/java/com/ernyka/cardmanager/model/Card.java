package com.ernyka.cardmanager.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Card implements Serializable {
    @Column(unique = true)
    private String uuid;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String ownerFirstName;
    @Column
    private String ownerLastName;
    @Column
    private Date expirationDate = new Date(System.currentTimeMillis() + 100L * 60 * 60 * 24 * 365);
    @Column
    private Integer cvv2 = Integer.valueOf(String.format("%04d", new Random().nextInt(9999)));
    @Column
    private String secondPass;
    @Column
    private Long credit;
    @Column
    private Boolean isDeleted = Boolean.FALSE;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<CardTransaction> transactions;
}
