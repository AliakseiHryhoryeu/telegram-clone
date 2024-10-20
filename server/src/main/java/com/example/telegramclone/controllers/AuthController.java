package com.example.telegramclone.controllers;

import com.example.telegramclone.models.User;
import com.example.telegramclone.models.Message;
import com.example.telegramclone.models.Contact;
import com.example.telegramclone.models.JwtPayload;
import com.example.telegramclone.repositories.UserRepository;
import com.example.telegramclone.utils.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import com.example.telegramclone.repositories.MessageRepository;

import com.example.telegramclone.DTO.Message.MessageCreateDTO;
import com.example.telegramclone.DTO.Message.MessageDeleteDTO;
import com.example.telegramclone.DTO.Message.MessageReadDTO;
import com.example.telegramclone.DTO.Message.MessageUpdateDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.example.telegramclone.DTO.Auth.AuthLoginDTO;
import com.example.telegramclone.DTO.Message.DeleteAllMessagesByContactIdDTO;
import com.example.telegramclone.DTO.Message.GetMessageDTO;
import com.example.telegramclone.DTO.Message.GetMessagesByContactIdDTO;
import com.example.telegramclone.utils.PasswordUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserRepository userRepository;

	// 1. Создание сообщения
	@PostMapping("/login")
	public ResponseEntity<?> authLogin(@RequestBody @Valid AuthLoginDTO req) {

		// Data validations
		Optional<User> userOpt;

		if (req.getLogin().contains("@")) {
			// Логин в формате email
			userOpt = userRepository.findByEmail(req.getLogin());
		} else {
			// Логин в формате username
			userOpt = userRepository.findByUsername(req.getLogin());
		}
		if (userOpt.isEmpty()) {
			return ResponseEntity.status(404).body("User not found");
		}

		User user = userOpt.get();

		if (!PasswordUtil.checkPassword(req.getPassword(), user.getPassword())) {
			return ResponseEntity.status(401).body("Password not valid");
		}

		String jwt = JwtUtil.createJwtSignedHMAC(user.getId(), user.getUsername(), user.getEmail());

		return ResponseEntity.status(201).body(jwt);
	}

	// Продление jwt
	@PostMapping("/newJwt")
	public ResponseEntity<?> authLogin(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

		Jws<Claims> jwtParsed; // Извлекаем токен, убирая "Bearer "
		if (authorizationHeader.startsWith("Bearer ")) {
			String jwt = authorizationHeader.substring(7);

			try {
				jwtParsed = JwtUtil.parseJwt(jwt);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT: " + e.getMessage());
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Authorization (jwt) header must start with 'Bearer '");
		}

		// Extract the claims from the JWT
		Claims claims = jwtParsed.getPayload();
		JwtPayload jwtPayload = new JwtPayload(
				claims.get("id", String.class),
				claims.get("username", String.class),
				claims.get("email", String.class),
				claims.getId(),
				claims.getIssuedAt() != null ? claims.getIssuedAt().getTime() : 0,
				claims.getExpiration() != null ? claims.getExpiration().getTime() : 0);

		// Data validations
		// Проверка наличия пользователей через экземпляры userRepository
		Optional<User> userOpt = userRepository.findById(jwtPayload.getId());

		if (userOpt.isEmpty()) {
			return ResponseEntity.status(404).body("User not found");
		}

		User user = userOpt.get();

		String jwt = JwtUtil.createJwtSignedHMAC(user.getId(), user.getUsername(), user.getEmail());

		return ResponseEntity.status(201).body(jwt);
	}
}
