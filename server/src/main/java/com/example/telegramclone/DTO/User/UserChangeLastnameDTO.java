package com.example.telegramclone.DTO.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserChangeLastnameDTO {

	@NotBlank(message = "lastname is required")
	@Size(min = 0, max = 30, message = "Maximum lastname size = 30 letters")
	private String lastname;

	public UserChangeLastnameDTO() {
	}

	public UserChangeLastnameDTO(String lastname) {

		this.lastname = lastname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}
