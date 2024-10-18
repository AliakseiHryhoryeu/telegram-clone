package com.example.telegramclone.controllers;

import com.example.telegramclone.DTO.User.UserCreateRequest;
import com.example.telegramclone.models.User;
import com.example.telegramclone.repositories.UserRepository;
import com.example.telegramclone.utils.PasswordUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;

import jakarta.validation.Valid;

import com.example.telegramclone.utils.JwtUtil;

import com.example.telegramclone.DTO.User.UserChangeDescriptionDTO;
import com.example.telegramclone.DTO.User.UserChangePasswordDTO;
import com.example.telegramclone.DTO.User.UserChangeUsernameDTO;
import com.example.telegramclone.DTO.User.UserCreateResponse;
import com.example.telegramclone.DTO.User.UserParseJwtResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateRequest req) {
        // Requirements fields
        //
        // Username validation
        if (req.getUsername() == null || req.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username must not be null or empty");
        }
        if (req.getUsername().length() < 3 || req.getUsername().length() > 35) {
            return ResponseEntity.badRequest().body("Username must be between 3 and 35 characters long");
        }

        // Email validation
        if (!req.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }
        if (req.getEmail() == null || req.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email must not be null or empty");
        }

        // Password validation
        if (req.getPassword() == null || req.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password must not be null or empty");
        }
        if (req.getPassword().length() < 8) {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters long");
        }
        if (req.getPassword().length() > 40) {
            return ResponseEntity.badRequest().body("Password max lenght 40 characters long");
        }

        // username is uniq?
        Optional<User> existingUsername = userRepository.findByUsername(req.getUsername());
        if (existingUsername.isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        // Email is uniq?
        Optional<User> existingUserEmail = userRepository.findByEmail(req.getEmail());
        if (existingUserEmail.isPresent()) {
            return ResponseEntity.badRequest().body("email already taken");
        }

        // Hashing password
        String hashedPassword = PasswordUtil.hashPassword(req.getPassword());

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(hashedPassword);

        userRepository.save(user);
        String jwt = JwtUtil.createJwtSignedHMAC(user.getId(), req.getUsername(), user.getEmail());
        UserCreateResponse userCreateResponse = new UserCreateResponse(user, jwt);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreateResponse);
    }

    @PostMapping("/parseJwt")
    public ResponseEntity<?> createJwt(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        // Предполагается, что JWT передается в формате "Bearer <token>"
        if (authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7); // Извлекаем токен, убирая "Bearer "

            // Парсинг JWT
            Jws<Claims> response;
            try {
                response = JwtUtil.parseJwt(jwt);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT: " + e.getMessage());
            }

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authorization header must start with 'Bearer '");
        }
    }

    @PostMapping("/changeUsername")
    public ResponseEntity<?> changeUsername(@RequestBody @Valid UserChangeUsernameDTO req) {
        // Data validations
        // Find user for id and email
        Optional<User> userOpt = userRepository.findById(req.getId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOpt.get();

        // Validation email from req.email and req.id
        if (!user.getEmail().equals(req.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email");
        }

        // New username is uniq?
        Optional<User> existingUser = userRepository.findByUsername(req.getNewusername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        // Refresh username and save
        user.setUsername(req.getNewusername());
        userRepository.save(user);

        return ResponseEntity.ok("Username updated successfully");
    }

    @GetMapping("/changePassword")
    public ResponseEntity<?> changeUsername(@RequestBody @Valid UserChangePasswordDTO req) {
        // Data validations
        // Find user for id and email
        Optional<User> userOpt = userRepository.findById(req.getId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOpt.get();

        // Validation email from req.email and req.id
        if (!user.getEmail().equals(req.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email");
        }

        // Vefefication OLD password старого пароля
        if (!PasswordUtil.checkPassword(req.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Old password is incorrect");
        }

        // Validation new Passwrod
        if (req.getNewPassword().length() < 8 || req.getNewPassword().length() > 30) {
            return ResponseEntity.badRequest().body("New password must be between 8 and 30 characters");
        }

        // Refresh username and save
        user.setPassword(req.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.ok("Username updated successfully");
    }

    @GetMapping("/changeDescription")
    public ResponseEntity<?> changeUsername(@RequestBody @Valid UserChangeDescriptionDTO req) {
        // Data validations
        // Find user for id and email
        Optional<User> userOpt = userRepository.findById(req.getId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOpt.get();

        // Validation email from req.email and req.id
        if (!user.getEmail().equals(req.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email");
        }

        // Validation new Description
        if (req.getNewDescription().length() > 2000) {
            return ResponseEntity.badRequest().body("Maximum description size = 2000 letters");
        }

        // Refresh description and save
        user.setUsername(req.getNewDescription());
        userRepository.save(user);

        return ResponseEntity.ok("Description updated successfully");
    }

    @GetMapping("/searchUsers")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String searchTerm) {
        // Поиск пользователей по имени или имени пользователя
        List<User> users = userRepository.searchByNameOrUsername(searchTerm);

        // Проверяем, найдены ли пользователи
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable String id) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    // delete later that handle
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
