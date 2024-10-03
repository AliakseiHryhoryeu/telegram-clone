package com.example.telegramclone.DTO.Message;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageReadDTO {

	@NotBlank(message = "messageId is required")
	private String messageId;

	@NotBlank(message = "toUserId is required")
	private String toUserId;
}
