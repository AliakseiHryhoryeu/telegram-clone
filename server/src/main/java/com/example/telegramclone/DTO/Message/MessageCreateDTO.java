package com.example.telegramclone.DTO.Message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MessageCreateDTO {

	@NotBlank(message = "message is required")
	@Size(min = 1, max = 5000, message = "message Minimum size = 1, Max size 5000 letters")
	private String message;

	@NotBlank(message = "from UserId is required")
	private String fromUserId;

	@NotBlank(message = "to UserId is required")
	private String toUserId;

	// Конструктор без аргументов
	public MessageCreateDTO() {
	}

	// Конструктор со всеми аргументами
	public MessageCreateDTO(String message, String fromUserId, String toUserId) {
		this.message = message;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
	}

	// Геттеры и сеттеры
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
}
