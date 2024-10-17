package com.example.telegramclone.DTO.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserChangeUsernameDTO {

	@NotBlank(message = "id is required")
	private String id;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Username is required")
	private String newusername;

	// Конструктор без аргументов
	public UserChangeUsernameDTO() {
	}

	// Конструктор со всеми аргументами
	public UserChangeUsernameDTO(String id, String email, String newusername) {
		this.id = id;
		this.email = email;
		this.newusername = newusername;
	}

	// Геттеры и сеттеры
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNewusername() {
		return newusername;
	}

	public void setNewusername(String newusername) {
		this.newusername = newusername;
	}
}
