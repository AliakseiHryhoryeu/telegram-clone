package com.example.telegramclone.DTO.Auth;

public class AuthLoginDTO {
	private String login;
	private String password;

	public AuthLoginDTO() {
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getLogin() {
		return login;
	}
}
