package com.example.telegramclone.DTO.Message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MessageUpdateDTO {

	@NotBlank(message = "newMessage is required")
	@Size(min = 1, max = 5000, message = "Message minimum size = 1, max size = 5000 letters")
	private String newMessage;

	@NotBlank(message = "fromUserId is required")
	private String fromUserId;

	// Конструктор без аргументов
	public MessageUpdateDTO() {
	}

	// Конструктор со всеми аргументами
	public MessageUpdateDTO(String newMessage, String fromUserId) {
		this.newMessage = newMessage;
		this.fromUserId = fromUserId;
	}

	// Геттеры и сеттеры
	public String getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(String newMessage) {
		this.newMessage = newMessage;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
}
