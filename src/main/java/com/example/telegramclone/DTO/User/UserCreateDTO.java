package com.example.telegramclone.DTO.User;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserCreateDTO {

	@NotBlank(message = "firstname is required")
	@Size(min = 1, max = 100, message = "For First name Minimum size = 1, Max size 100 letters")
	private String firstname;

	@Size(min = 1, max = 100, message = "For First name Minimum size = 1, Max size 100 letters")
	private String lastname;

	@NotBlank(message = "Username is required")
	private String username;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
	private String password;
}
