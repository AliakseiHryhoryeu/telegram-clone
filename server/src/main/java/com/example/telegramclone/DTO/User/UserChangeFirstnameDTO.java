package com.example.telegramclone.DTO.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserChangeFirstnameDTO {

	@NotBlank(message = "firstname is required")
	@Size(min = 0, max = 30, message = "Maximum firstname size = 30 letters")
	private String firstname;

	public UserChangeFirstnameDTO() {
	}

	public UserChangeFirstnameDTO(String firstname) {

		this.firstname = firstname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
}
