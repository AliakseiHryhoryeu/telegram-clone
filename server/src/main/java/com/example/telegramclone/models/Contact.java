package com.example.telegramclone.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contacts")
public class Contact {
	@Id
	private String contactId; // Убедитесь, что это поле есть
	private String userId; // ID пользователя, к которому принадлежит контакт
	private String name; // Имя контакта (если нужно)

	// Геттеры и сеттеры
	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
