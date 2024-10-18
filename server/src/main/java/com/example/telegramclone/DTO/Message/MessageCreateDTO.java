package com.example.telegramclone.DTO.Message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MessageCreateDTO {

	@NotBlank(message = "userId is required")
	private String toUserId;
	@NotBlank(message = "message is required")
	@Size(min = 1, max = 5000, message = "message Minimum size = 1, Max size 5000 letters")
	private String message;

	// Конструктор без аргументов
	public MessageCreateDTO() {
	}

	// Конструктор со всеми аргументами
	public MessageCreateDTO(String message, String toUserId) {
		this.toUserId = toUserId;
		this.message = message;

	}

	// Геттеры и сеттеры
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
}
