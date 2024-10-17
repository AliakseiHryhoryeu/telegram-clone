package com.example.telegramclone.DTO.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserChangePasswordDTO {

	@NotBlank(message = "id is required")
	private String id;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Old password is required")
	private String oldPassword;

	@NotBlank(message = "New password is required")
	private String newPassword;

	// Конструктор без аргументов
	public UserChangePasswordDTO() {
	}

	// Конструктор со всеми аргументами
	public UserChangePasswordDTO(String id, String email, String oldPassword, String newPassword) {
		this.id = id;
		this.email = email;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
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

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
