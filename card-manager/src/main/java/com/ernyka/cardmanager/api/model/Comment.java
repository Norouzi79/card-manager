package com.ernyka.cardmanager.api.model;

import lombok.Getter;

@Getter
public class Comment {
    private Integer id;
    private String body;
    private Integer postId;
}
