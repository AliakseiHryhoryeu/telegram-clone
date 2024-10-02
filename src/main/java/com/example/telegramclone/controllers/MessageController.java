package com.example.telegramclone.controllers;

import com.example.telegramclone.models.Message;
import com.example.telegramclone.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/create")
    public Message createMessage(@RequestBody Message message) {
        return messageRepository.save(message);
    }

    @GetMapping("/between/{fromUserId}/{toUserId}")
    public List<Message> getMessagesBetweenUsers(@PathVariable String fromUserId, @PathVariable String toUserId) {
        return messageRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
    }
}
