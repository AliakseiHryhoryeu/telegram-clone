package com.example.telegramclone.DTO.Message;

import javax.validation.constraints.NotBlank;

public class MessageReadDTO {

	@NotBlank(message = "messageId is required")
	private String messageId;

	@NotBlank(message = "toUserId is required")
	private String toUserId;

	// Конструктор без аргументов
	public MessageReadDTO() {
	}

	// Конструктор со всеми аргументами
	public MessageReadDTO(String messageId, String toUserId) {
		this.messageId = messageId;
		this.toUserId = toUserId;
	}

	// Геттеры и сеттеры
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
}
