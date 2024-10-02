package com.example.telegramclone.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    private String message;
    private String fromUserId;
    private String toUserId;
    private String date;
    private String messageType;
    private String messageStatus;
}
