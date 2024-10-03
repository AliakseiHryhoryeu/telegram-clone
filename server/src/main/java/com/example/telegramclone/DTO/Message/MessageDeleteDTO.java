package com.example.telegramclone.DTO.Message;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageDeleteDTO {

	@NotBlank(message = "messageId is required")
	private String messageId;

	@NotBlank(message = "from UserId is required")
	private String fromUserId;
}
