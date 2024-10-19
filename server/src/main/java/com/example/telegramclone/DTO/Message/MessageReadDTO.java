package com.example.telegramclone.DTO.Message;

import javax.validation.constraints.NotBlank;

public class MessageReadDTO {

	@NotBlank(message = "messageId is required")
	private String messageId;

	// Конструктор без аргументов
	public MessageReadDTO() {
	}

	// Конструктор со всеми аргументами
	public MessageReadDTO(String messageId) {
		this.messageId = messageId;
	}

	// Геттеры и сеттеры
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

}
