package com.example.telegramclone.DTO.Message;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MessageUpdateDTO {

	@NotBlank(message = "newMessage is required")
	@Size(min = 1, max = 5000, message = "Message minimum size = 1, max size = 5000 letters")
	private String newMessage;

	@NotBlank(message = "fromUserId is required")
	private String fromUserId;
}
