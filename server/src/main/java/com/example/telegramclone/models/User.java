package com.example.telegramclone.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

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
    private LocalDateTime lastSeen;
    private List<Contact> contacts;
    private List<String> blocked;
    private List<String> messages;

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void updateContact(String userId, String messageId) {
        // Инициализируем список контактов, если он равен null
        if (this.contacts == null) {
            this.contacts = new ArrayList<>();
        }

        // Ищем контакт по userId в списке контактов пользователя
        Contact existingContact = this.contacts.stream()
                .filter(c -> c.getUserId().equals(userId))
                .findFirst()
                .orElse(null);

        if (existingContact != null) {
            // Если контакт найден, добавляем идентификатор нового сообщения
            existingContact.addMessageId(messageId);
        } else {
            // Если контакт не найден, создаем новый контакт и добавляем в список
            Contact newContact = new Contact();
            newContact.setUserId(userId);
            newContact.addMessageId(messageId);
            this.contacts.add(newContact);
        }
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void addBlocked(String id) {
        this.blocked.add(id);
    }

    public void removeBlocked(String id) {
        this.blocked.remove(id);
    }

    public List<String> getBlocked() {
        return blocked;
    }
}
