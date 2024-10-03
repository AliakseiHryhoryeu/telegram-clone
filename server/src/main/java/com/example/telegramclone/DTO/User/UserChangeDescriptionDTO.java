package com.example.telegramclone.DTO.User;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserChangeDescriptionDTO {

	@NotBlank(message = "id is required")
	private String id;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Description is required")
	@Size(min = 0, max = 2000, message = "Maximum description size = 2000 letters")
	private String newDescription;

}
