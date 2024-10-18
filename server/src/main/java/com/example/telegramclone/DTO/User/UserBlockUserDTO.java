package com.example.telegramclone.DTO.User;

import javax.validation.constraints.NotBlank;

public class UserBlockUserDTO {

	@NotBlank
	private String id;

	// Конструктор без аргументов
	public UserBlockUserDTO() {
	}

	// Конструктор со всеми аргументами
	public UserBlockUserDTO(String id) {

		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
