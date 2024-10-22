package com.example.telegramclone.DTO.User;

import javax.validation.constraints.NotBlank;

public class UserBlockUserDTO {

	@NotBlank
	private String userId;

	// Конструктор без аргументов
	public UserBlockUserDTO() {
	}

	// Конструктор со всеми аргументами
	public UserBlockUserDTO(String userId) {

		this.userId = userId;
	}

	public String getId() {
		return userId;
	}

	public void setId(String userId) {
		this.userId = userId;
	}

}
