package com.example.telegramclone.DTO.User;

import javax.validation.constraints.NotBlank;

public class UserChangeUsernameDTO {

	@NotBlank(message = "Username is required")
	private String newusername;

	// Конструктор без аргументов
	public UserChangeUsernameDTO() {
	}

	// Конструктор со всеми аргументами
	public UserChangeUsernameDTO(String newusername) {
		this.newusername = newusername;
	}

	public String getNewusername() {
		return newusername;
	}

	public void setNewusername(String newusername) {
		this.newusername = newusername;
	}
}
