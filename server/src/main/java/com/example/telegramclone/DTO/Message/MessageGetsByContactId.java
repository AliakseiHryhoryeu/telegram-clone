package com.example.telegramclone.DTO.Message;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageGetsByContactId {

	@NotBlank(message = "from UserId is required")
	private String fromUserId;

	@NotBlank(message = "contactId is required")
	private String contactId;

}
