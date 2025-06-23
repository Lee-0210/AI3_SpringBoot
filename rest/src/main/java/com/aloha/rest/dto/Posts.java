package com.aloha.rest.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Posts implements Serializable {

    private Long no;
    private String id;
    private String title;
    private String writer;
    private String content;
    private Date createdAt;
    private Date updatedAt;

    public Posts() {
        this.id = UUID.randomUUID().toString();
    }
}