package com.example.telegramclone.DTO.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserChangeDescriptionDTO {

	@NotBlank(message = "id is required")
	private String id;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Description is required")
	@Size(min = 0, max = 2000, message = "Maximum description size = 2000 letters")
	private String newDescription;

	// Конструктор без аргументов
	public UserChangeDescriptionDTO() {
	}

	// Конструктор со всеми аргументами
	public UserChangeDescriptionDTO(String id, String email, String newDescription) {
		this.id = id;
		this.email = email;
		this.newDescription = newDescription;
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

	public String getNewDescription() {
		return newDescription;
	}

	public void setNewDescription(String newDescription) {
		this.newDescription = newDescription;
	}
}
