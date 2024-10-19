package com.example.telegramclone.DTO.Message;

public class GetMessageDTO {
	private String messageId;

	public GetMessageDTO() {

	}

	public GetMessageDTO(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
}
