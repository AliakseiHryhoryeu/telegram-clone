package com.example.telegramclone.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "groups")
public class Group {
    @Id
    private String id;
    private String groupName;
    private String groupType;
    private String title;
    private String description;
    private String ownerId;
    private List<String> users;
}
