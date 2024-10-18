package com.example.telegramclone.DTO.User;

import javax.validation.constraints.NotBlank;

public class UserChangePasswordDTO {

	@NotBlank(message = "Old password is required")
	private String oldPassword;

	@NotBlank(message = "New password is required")
	private String newPassword;

	// Конструктор без аргументов
	public UserChangePasswordDTO() {
	}

	// Конструктор со всеми аргументами
	public UserChangePasswordDTO(String oldPassword, String newPassword) {

		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
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
