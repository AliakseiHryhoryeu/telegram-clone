package com.example.telegramclone.DTO.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserCreateRequest {

	@NotBlank
	@Size(min = 3, max = 35, message = "Username must be between 3 and 35 characters long")
	private String username;

	@NotBlank
	private String email;

	@NotBlank
	@Size(min = 8, max = 40, message = "Password must be between 8 and 40 characters long")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
