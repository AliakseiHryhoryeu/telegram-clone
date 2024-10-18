package com.example.telegramclone.DTO.Message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MessageUpdateDTO {

	@NotBlank()
	private String messageId;
	@NotBlank(message = "newMessage is required")
	@Size(min = 1, max = 5000, message = "Message minimum size = 1, max size = 5000 letters")
	private String message;

	// Конструктор без аргументов
	public MessageUpdateDTO() {
	}

	// Конструктор со всеми аргументами
	public MessageUpdateDTO(String messageId, String message) {
		this.messageId = messageId;
		this.message = message;
	}

	// Геттеры и сеттеры
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageId() {
		return messageId;
	}
}
