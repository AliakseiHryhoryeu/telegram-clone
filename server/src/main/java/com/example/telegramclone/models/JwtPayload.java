package com.example.telegramclone.models;

public class JwtPayload {
	private String id;
	private String username;
	private String email;
	private String jti; // JWT ID
	private long iat; // issued at
	private long exp; // expiration

	public JwtPayload(String id, String username, String email, String jti, long iat, long exp) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.jti = jti;
		this.iat = iat;
		this.exp = exp;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getJti() {
		return jti;
	}

	public long getIat() {
		return iat;
	}

	public long getExp() {
		return exp;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public void setIat(long iat) {
		this.iat = iat;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}
}
