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
import com.example.telegramclone.DTO.Message.MessageReadDTO;
import com.example.telegramclone.DTO.Message.MessageUpdateDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.example.telegramclone.DTO.Message.DeleteAllMessagesByContactIdDTO;
import com.example.telegramclone.DTO.Message.GetMessageDTO;
import com.example.telegramclone.DTO.Message.GetMessagesByContactIdDTO;

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

        fromUser.updateContact(toUser.getId(), message.getId());
        toUser.updateContact(fromUser.getId(), message.getId());
        // Обновляем контакты для пользователя fromUser и toUser

        userRepository.save(fromUser);
        userRepository.save(toUser);

        return ResponseEntity.status(201).body("Message created and contacts updated successfully");
    }

    // 2. get messages by contact id
    @PostMapping("/getMessagesByContactId")
    public ResponseEntity<?> getMessagesByContactId(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody GetMessagesByContactIdDTO req) {

        // Parse the JWT
        Jws<Claims> jwtParsed;
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

        Optional<User> userOpt = userRepository.findById(jwtPayload.getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bad request");
        }
        User user = userOpt.get();

        // Find the contact by contactId (assuming user has a list of contacts)
        Optional<Contact> contactOpt = user.getContacts().stream()
                .filter(item -> item.getUserId().equals(req.getContactId()))
                .findAny();

        if (contactOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact not found11");
        }

        Contact contact = contactOpt.get();

        // Fetch messages based on the IDs in the contact's messages list
        List<Message> messages = messageRepository.findAllById(contact.getMessagesIdFromContact());

        // Format the response
        List<Map<String, Object>> responseMessages = new ArrayList<>();
        for (Message message : messages) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("id", message.getId());
            messageData.put("fromUserId", message.getFromUserId());
            messageData.put("toUserId", message.getToUserId());
            messageData.put("message", message.getMessage());
            messageData.put("date", message.getDate());
            messageData.put("messageStatus", message.getMessageStatus());
            responseMessages.add(messageData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("contacts", List.of(Map.of("userid", contact.getUserId(), "messages", responseMessages)));

        return ResponseEntity.ok(response);
    }

    // 3. Редактирование сообщения
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

    // 3. Редактирование сообщения
    @PostMapping("/DeleteMessage")
    public ResponseEntity<?> DeleteMessage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid MessageDeleteDTO req) {

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

        Optional<Message> messageOpt = messageRepository.findById(req.getMessageId());

        if (messageOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Message not found");
        }

        Message message = messageOpt.get();

        // Extract the claims from the JWT
        Claims claims = jwtParsed.getPayload();
        JwtPayload jwtPayload = new JwtPayload(
                claims.get("id", String.class),
                claims.get("username", String.class),
                claims.get("email", String.class),
                claims.getId(),
                claims.getIssuedAt() != null ? claims.getIssuedAt().getTime() : 0,
                claims.getExpiration() != null ? claims.getExpiration().getTime() : 0);

        // Проверка, что удаляющий пользователь является автором сообщения
        if (!message.getFromUserId().equals(jwtPayload.getId())) {
            return ResponseEntity.status(403).body("You are not allowed to delete this message");
        }

        // find 2 users
        Optional<User> fromUserOpt = userRepository.findById(message.getFromUserId());
        Optional<User> toUserOpt = userRepository.findById(message.getToUserId());

        // delete messageid from users contacts
        if (fromUserOpt.isPresent()) {
            User fromUser = fromUserOpt.get();
            // fromUser.getMessages().remove(message.getId());
            userRepository.save(fromUser);
        }

        if (toUserOpt.isPresent()) {
            User toUser = toUserOpt.get();
            // toUser.getMessages().remove(message.getId());
            userRepository.save(toUser);
        }

        // Delete message from database
        messageRepository.delete(message);

        return ResponseEntity.ok("Message deleted successfully");
    }

    // Get message
    @PostMapping("/GetMessage")
    public ResponseEntity<?> GetMessage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid GetMessageDTO req) {

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

        // Get Message
        Optional<Message> messageOpt = messageRepository.findById(req.getMessageId());
        if (messageOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Message not found");
        }

        Message message = messageOpt.get();
        // Проверка, что пользователь является автором сообщения
        if (!message.getFromUserId().equals(jwtPayload.getId()) || !message.getToUserId().equals(jwtPayload.getId())) {
            return ResponseEntity.status(403).body("You are not allowed to read this message");
        }

        return ResponseEntity.ok(message);
    }

    // 7. Пометка сообщения как "прочитанное"
    // Get message
    @PostMapping("/readMessage")
    public ResponseEntity<?> ReadMessage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid MessageReadDTO req) {

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

        // Get Message
        Optional<Message> messageOpt = messageRepository.findById(req.getMessageId());
        if (messageOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Message not found");
        }
        Message message = messageOpt.get();

        // Проверка, что получатель сообщения соответствует toUserId
        if (!message.getToUserId().equals(jwtPayload.getId())) {
            return ResponseEntity.status(403).body("You are not allowed to mark this message as read");
        }

        // Пометить сообщение как "прочитанное" (обновляем статус сообщения)
        message.setMessageStatus("read");
        messageRepository.save(message);

        return ResponseEntity.ok("Message marked as read successfully");
    }

    @PostMapping("/DeleteAllMessagesByContactId")
    public ResponseEntity<?> DeleteAllMessagesByContactId(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid DeleteAllMessagesByContactIdDTO req) {

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

        // Извлекаем данные из токена
        Claims claims = jwtParsed.getPayload();
        JwtPayload jwtPayload = new JwtPayload(
                claims.get("id", String.class),
                claims.get("username", String.class),
                claims.get("email", String.class),
                claims.getId(),
                claims.getIssuedAt() != null ? claims.getIssuedAt().getTime() : 0,
                claims.getExpiration() != null ? claims.getExpiration().getTime() : 0);

        // Найдем пользователя по ID из токена
        Optional<User> userOpt = userRepository.findById(jwtPayload.getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = userOpt.get();

        // Find the contact IN USER by contactId (assuming user has a list of contacts)
        Optional<Contact> userContactOpt = user.getContacts().stream()
                .filter(item -> item.getUserId().equals(req.getContactId()))
                .findAny();

        if (userContactOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact not found11");
        }
        Contact userContact = userContactOpt.get();

        // Найдем 2 пользователя по ID из токена
        Optional<User> contactOpt = userRepository.findById(userContact.getUserId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User contact = contactOpt.get();

        // Find the contact IN USER by contactId (assuming user has a list of contacts)
        Optional<Contact> contactUserOpt = contact.getContacts().stream()
                .filter(item -> item.getUserId().equals(user.getId()))
                .findAny();

        if (userContactOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact not found11");
        }
        Contact contactUser = contactUserOpt.get();

        // Получаем список идентификаторов сообщений
        List<String> userdMessagesIds = userContact.getMessagesIdFromContact();

        // Очищаем сообщения в контактах
        userContact.getMessagesIdFromContact().clear();
        contactUser.getMessagesIdFromContact().clear();

        // // Удаляем сообщения из базы данных
        // if (!userdMessagesIds.isEmpty()) {
        // messageRepository.deleteAllByIdIn(userdMessagesIds);
        // }

        // Удаление сообщений
        if (!userdMessagesIds.isEmpty()) {
            // Выводим ID сообщений, которые должны быть удалены
            System.out.println("Message IDs to delete: " + userdMessagesIds);

            // Находим сообщения перед удалением
            List<Message> messagesToDelete = messageRepository.findAllById(userdMessagesIds);
            System.out.println("Messages found for deletion: " + messagesToDelete);

            // Удаляем сообщения
            messageRepository.deleteAllByIdIn(userdMessagesIds);
            System.out.println("Messages deleted successfully");
        } else {
            System.out.println("No message IDs found to delete");
        }

        // Сохраняем изменения пользователя
        userRepository.save(user);
        userRepository.save(contact);

        return ResponseEntity.ok("All messages between users have been deleted.");
    }

    // // 4. Получение сообщений по ID контакта (собеседника)
    // @PostMapping("/getMessagesByContactId")
    // public ResponseEntity<?> getMessagesByContactId(@RequestBody @Valid
    // MessageGetsByContactIdDTO request) {
    // // Проверка, существует ли пользователь
    // Optional<User> fromUserOpt =
    // userRepository.findById(request.getFromUserId());
    // if (fromUserOpt.isEmpty()) {
    // return ResponseEntity.status(404).body("User not found");
    // }

    // User fromUser = fromUserOpt.get();

    // // // Проверка, существует ли контакт в списке контактов пользователя
    // // boolean contactExists = fromUser.getContacts().stream()
    // // .anyMatch(contact -> contact.g().equals(request.getContactId()));

    // // if (!contactExists) {
    // // return ResponseEntity.status(403).body("Contact not found in your
    // contacts");
    // // }

    // // Получение сообщений между пользователем и контактом
    // List<Message> messages =
    // messageRepository.findMessagesByFromUserIdAndToUserId(
    // request.getFromUserId(), request.getContactId());

    // return ResponseEntity.ok(messages);
    // }

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

    // @GetMapping("/between/{fromUserId}/{toUserId}")
    // public List<Message> getMessagesBetweenUsers(@PathVariable String fromUserId,
    // @PathVariable String toUserId) {
    // return messageRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
    // }

}
