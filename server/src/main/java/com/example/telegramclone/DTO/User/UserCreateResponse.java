package com.example.telegramclone.DTO.User;

import com.example.telegramclone.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCreateResponse {

	@JsonProperty("user")
	private User user;

	@JsonProperty("jwt")
	private String jwt;

	public UserCreateResponse(User user, String jwt) {
		this.user = user;
		this.jwt = jwt;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

}
