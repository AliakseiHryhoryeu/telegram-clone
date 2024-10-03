package com.example.telegramclone.DTO.User;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserGetContactsDTO {

	@NotBlank(message = "id is required")
	private String id;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

}
