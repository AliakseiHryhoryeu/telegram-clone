package com.example.telegramclone.DTO.Message;

import javax.validation.constraints.NotBlank;

public class MessageDeleteDTO {

	@NotBlank(message = "messageId is required")
	private String messageId;

	@NotBlank(message = "from UserId is required")
	private String fromUserId;

	// Конструктор без аргументов
	public MessageDeleteDTO() {
	}

	// Конструктор со всеми аргументами
	public MessageDeleteDTO(String messageId, String fromUserId) {
		this.messageId = messageId;
		this.fromUserId = fromUserId;
	}

	// Геттеры и сеттеры
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
}
