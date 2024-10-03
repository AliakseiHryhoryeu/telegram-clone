package com.example.telegramclone.DTO.Message;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MessageCreateDTO {

	@NotBlank(message = "message is required")
	@Size(min = 1, max = 5000, message = "message Minimum size = 1, Max size 5000 letters")
	private String message;

	@NotBlank(message = "from UserId is required")
	private String fromUserId;

	@NotBlank(message = "to UserId is required")
	private String toUserId;
}
