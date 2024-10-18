package com.example.telegramclone.controllers;

import com.example.telegramclone.models.User;
import com.example.telegramclone.models.Message;
import com.example.telegramclone.models.Contact;
import com.example.telegramclone.models.JwtPayload;
import com.example.telegramclone.repositories.UserRepository;
import com.example.telegramclone.utils.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import com.example.telegramclone.repositories.MessageRepository;

import com.example.telegramclone.DTO.Message.MessageCreateDTO;
import com.example.telegramclone.DTO.Message.MessageDeleteDTO;
import com.example.telegramclone.DTO.Message.MessageGetsByContactIdDTO;
import com.example.telegramclone.DTO.Message.MessageReadDTO;
import com.example.telegramclone.DTO.Message.MessageUpdateDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Создание сообщения
    @PostMapping("/createMessage")
    public ResponseEntity<?> createMessage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid MessageCreateDTO req) {

        Jws<Claims> jwtParsed; // Извлекаем токен, убирая "Bearer "
        if (authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);

            try {
                jwtParsed = JwtUtil.parseJwt(jwt);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT: " + e.getMessage());
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authorization (jwt) header must start with 'Bearer '");
        }

        // Extract the claims from the JWT
        Claims claims = jwtParsed.getPayload();
        JwtPayload jwtPayload = new JwtPayload(
                claims.get("id", String.class),
                claims.get("username", String.class),
                claims.get("email", String.class),
                claims.getId(),
                claims.getIssuedAt() != null ? claims.getIssuedAt().getTime() : 0,
                claims.getExpiration() != null ? claims.getExpiration().getTime() : 0);

        // Data validations
        // Проверка наличия пользователей через экземпляры userRepository
        Optional<User> fromUserOpt = userRepository.findById(jwtPayload.getId());
        Optional<User> toUserOpt = userRepository.findById(req.getToUserId());

        if (fromUserOpt.isEmpty() || toUserOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User fromUser = fromUserOpt.get();
        User toUser = toUserOpt.get();

        // Создаем новый объект сообщения
        Message message = new Message();
        message.setMessage(req.getMessage()); // Установка сообщения
        message.setFromUserId(fromUser.getId());
        message.setToUserId(req.getToUserId());
        message.setDate(new Date().toString()); // Установка текущей даты
        message.setMessageStatus("sent"); // Статус сообщения "sent"

        // Сохраняем сообщение в базе данных
        messageRepository.save(message);

        fromUser.updateContact(fromUser.getId(), message.getId());
        toUser.updateContact(toUser.getId(), message.getId());
        // Обновляем контакты для пользователя fromUser и toUser

        userRepository.save(fromUser);
        userRepository.save(toUser);

        return ResponseEntity.status(201).body("Message created and contacts updated successfully");
    }

    // 2. Редактирование сообщения
    @PostMapping("/updateMessage")
    public ResponseEntity<?> updateMessage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid MessageUpdateDTO req) {

        Jws<Claims> jwtParsed; // Извлекаем токен, убирая "Bearer "
        if (authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);

            try {
                jwtParsed = JwtUtil.parseJwt(jwt);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT: " + e.getMessage());
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authorization (jwt) header must start with 'Bearer '");
        }

        // Extract the claims from the JWT
        Claims claims = jwtParsed.getPayload();
        JwtPayload jwtPayload = new JwtPayload(
                claims.get("id", String.class),
                claims.get("username", String.class),
                claims.get("email", String.class),
                claims.getId(),
                claims.getIssuedAt() != null ? claims.getIssuedAt().getTime() : 0,
                claims.getExpiration() != null ? claims.getExpiration().getTime() : 0);

        // Поиск сообщения по его id
        Optional<Message> messageOpt = messageRepository.findById(req.getMessageId());

        if (messageOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Message not found");
        }

        Message message = messageOpt.get();
        if (!message.getFromUserId().equals(jwtPayload.getId())) {
            return ResponseEntity.badRequest().body("Wrong Request"); // Return a bad request response
        }
        // Обновляем текст сообщения
        message.setMessage(req.getMessage());

        // Сохраняем обновленное сообщение
        messageRepository.save(message);

        return ResponseEntity.ok("Message updated successfully");
    }

    // Ручка для удаления сообщения
    @DeleteMapping("/deleteMessage")
    public ResponseEntity<?> deleteMessage(@RequestBody @Valid MessageDeleteDTO messageDeleteDTO) {
        // Поиск сообщения по его id
        Optional<Message> messageOpt = messageRepository.findById(messageDeleteDTO.getMessageId());

        if (messageOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Message not found");
        }

        Message message = messageOpt.get();

        // Проверка, что удаляющий пользователь является автором сообщения
        if (!message.getFromUserId().equals(messageDeleteDTO.getFromUserId())) {
            return ResponseEntity.status(403).body("You are not allowed to delete this message");
        }

        // Удаление сообщения из базы данных
        messageRepository.delete(message);

        // Обновление списка сообщений у обоих пользователей
        Optional<User> fromUserOpt = userRepository.findById(message.getFromUserId());
        Optional<User> toUserOpt = userRepository.findById(message.getToUserId());

        if (fromUserOpt.isPresent()) {
            User fromUser = fromUserOpt.get();
            // Удаляем ID сообщения из списка сообщений отправителя
            fromUser.getMessages().remove(message.getId());
            userRepository.save(fromUser);
        }

        if (toUserOpt.isPresent()) {
            User toUser = toUserOpt.get();
            // Удаляем ID сообщения из списка сообщений получателя
            toUser.getMessages().remove(message.getId());
            userRepository.save(toUser);
        }

        return ResponseEntity.ok("Message deleted successfully");
    }

    // 4. Получение сообщений по ID контакта (собеседника)
    @PostMapping("/getMessagesByContactId")
    public ResponseEntity<?> getMessagesByContactId(@RequestBody @Valid MessageGetsByContactIdDTO request) {
        // Проверка, существует ли пользователь
        Optional<User> fromUserOpt = userRepository.findById(request.getFromUserId());
        if (fromUserOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User fromUser = fromUserOpt.get();

        // // Проверка, существует ли контакт в списке контактов пользователя
        // boolean contactExists = fromUser.getContacts().stream()
        // .anyMatch(contact -> contact.g().equals(request.getContactId()));

        // if (!contactExists) {
        // return ResponseEntity.status(403).body("Contact not found in your contacts");
        // }

        // Получение сообщений между пользователем и контактом
        List<Message> messages = messageRepository.findMessagesByFromUserIdAndToUserId(
                request.getFromUserId(), request.getContactId());

        return ResponseEntity.ok(messages);
    }

    // // 6. Loading messages with pagination
    // @GetMapping("/getMessagesPagination")
    // public ResponseEntity<List<Message>> getMessagesPagination(@RequestParam
    // String fromUserId,
    // @RequestParam String toUserId, @RequestParam int page, @RequestParam int
    // size) {
    // // Простая пагинация
    // List<Message> messages =
    // messageRepository.findByFromUserIdAndToUserId(fromUserId, toUserId)
    // .stream()
    // .skip(page * size)
    // .limit(size)
    // .toList();
    // return ResponseEntity.ok(messages);
    // }

    // 7. Пометка сообщения как "прочитанное"
    @PostMapping("/readMessage")
    public ResponseEntity<?> readMessage(@RequestBody @Valid MessageReadDTO messageReadDTO) {
        // Поиск сообщения по его ID
        Optional<Message> messageOpt = messageRepository.findById(messageReadDTO.getMessageId());

        if (messageOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Message not found");
        }

        Message message = messageOpt.get();

        // Проверка, что получатель сообщения соответствует toUserId
        if (!message.getToUserId().equals(messageReadDTO.getToUserId())) {
            return ResponseEntity.status(403).body("You are not allowed to mark this message as read");
        }

        // Пометить сообщение как "прочитанное" (обновляем статус сообщения)
        message.setMessageStatus("read"); // Предположим, что вы используете поле messageStatus
        messageRepository.save(message);

        return ResponseEntity.ok("Message marked as read successfully");
    }

    // @PostMapping("/create")
    // public Message createMessage(@RequestBody Message message) {
    // return messageRepository.save(message);
    // }

    // @GetMapping("/between/{fromUserId}/{toUserId}")
    // public List<Message> getMessagesBetweenUsers(@PathVariable String fromUserId,
    // @PathVariable String toUserId) {
    // return messageRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
    // }

}
