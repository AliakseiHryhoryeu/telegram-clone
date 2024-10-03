package com.example.telegramclone.controllers;

import com.example.telegramclone.models.Message;
import com.example.telegramclone.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    // 1. Создание сообщения
    @PostMapping("/createMessage")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        messageRepository.save(message);
        return ResponseEntity.status(201).body("Message created successfully");
    }

    // 2. Редактирование сообщения
    @PutMapping("/editMessage/{id}")
    public ResponseEntity<?> editMessage(@PathVariable String id, @RequestBody Message newMessageData) {
        Optional<Message> existingMessage = messageRepository.findById(id);
        if (existingMessage.isEmpty()) {
            return ResponseEntity.status(404).body("Message not found");
        }
        Message message = existingMessage.get();
        message.setMessage(newMessageData.getMessage());
        messageRepository.save(message);
        return ResponseEntity.ok("Message updated successfully");
    }

    // 3. Удаление сообщения
    @DeleteMapping("/deleteMessage/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable String id) {
        Optional<Message> existingMessage = messageRepository.findById(id);
        if (existingMessage.isEmpty()) {
            return ResponseEntity.status(404).body("Message not found");
        }
        messageRepository.deleteById(id);
        return ResponseEntity.ok("Message deleted successfully");
    }

    // 4. Получение сообщений по ID контакта (собеседника)
    @GetMapping("/getMessagesByContactId")
    public ResponseEntity<List<Message>> getMessagesByContactId(@RequestParam String fromUserId,
            @RequestParam String toUserId) {
        List<Message> messages = messageRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
        return ResponseEntity.ok(messages);
    }

    // 5. Получение сообщений по ID группы (если у вас будут групповые чаты)
    @GetMapping("/getMessagesByGroupId")
    public ResponseEntity<?> getMessagesByGroupId(@RequestParam String groupId) {
        // Данная ручка предполагает наличие групповых сообщений в структуре приложения
        // Необходима доработка, если у вас есть групповые чаты с отдельными группами.
        return ResponseEntity.status(501).body("Feature not implemented");
    }

    // 6. Подгрузка сообщений с пагинацией
    @GetMapping("/getMessagesPagination")
    public ResponseEntity<List<Message>> getMessagesPagination(@RequestParam String fromUserId,
            @RequestParam String toUserId, @RequestParam int page, @RequestParam int size) {
        // Простая пагинация
        List<Message> messages = messageRepository.findByFromUserIdAndToUserId(fromUserId, toUserId)
                .stream()
                .skip(page * size)
                .limit(size)
                .toList();
        return ResponseEntity.ok(messages);
    }

    // 7. Пометка сообщения как "прочитанное"
    @PostMapping("/readMessage/{id}")
    public ResponseEntity<?> readMessage(@PathVariable String id) {
        Optional<Message> messageOpt = messageRepository.findById(id);
        if (messageOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Message not found");
        }
        Message message = messageOpt.get();
        message.setMessageStatus("read"); // Assuming you have "messageStatus" to track read/unread status
        messageRepository.save(message);
        return ResponseEntity.ok("Message marked as read");
    }

    // @PostMapping("/create")
    // public Message createMessage(@RequestBody Message message) {
    // return messageRepository.save(message);
    // }

    @GetMapping("/between/{fromUserId}/{toUserId}")
    public List<Message> getMessagesBetweenUsers(@PathVariable String fromUserId, @PathVariable String toUserId) {
        return messageRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
    }

}
