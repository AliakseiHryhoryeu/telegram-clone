package com.example.telegramclone.DTO.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserChangeDescriptionDTO {

	@NotBlank(message = "Description is required")
	@Size(min = 0, max = 2000, message = "Maximum description size = 2000 letters")
	private String newDescription;

	public UserChangeDescriptionDTO() {
	}

	public UserChangeDescriptionDTO(String newDescription) {

		this.newDescription = newDescription;
	}

	public String getNewDescription() {
		return newDescription;
	}

	public void setNewDescription(String newDescription) {
		this.newDescription = newDescription;
	}
}
