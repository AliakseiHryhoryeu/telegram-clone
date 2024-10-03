package com.example.telegramclone.repositories;

import com.example.telegramclone.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByFromUserIdAndToUserId(String fromUserId, String toUserId);

    List<Message> findByToUserId(String toUserId);

    List<Message> findByFromUserId(String fromUserId);
}