package com.example.telegramclone.DTO.Message;

import javax.validation.constraints.NotBlank;

public class MessageGetsByContactIdDTO {

	@NotBlank(message = "from UserId is required")
	private String fromUserId;

	@NotBlank(message = "contactId is required")
	private String contactId;

	// Конструктор без аргументов
	public MessageGetsByContactIdDTO() {
	}

	// Конструктор со всеми аргументами
	public MessageGetsByContactIdDTO(String fromUserId, String contactId) {
		this.fromUserId = fromUserId;
		this.contactId = contactId;
	}

	// Геттеры и сеттеры
	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
}
