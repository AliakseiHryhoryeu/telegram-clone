package com.example.telegramclone.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String description;
    private String username;

    private String email;
    private String password;
    private String profileImageUrl;
    private boolean isOnline;
    private String lastSeen;
    private List<Contact> contacts;
    private List<String> messages;
}
