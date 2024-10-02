package com.example.telegramclone.controllers;

import com.example.telegramclone.models.User;
import com.example.telegramclone.repositories.UserRepository;
import com.example.telegramclone.utils.PasswordUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

@PostMapping("/create")
public ResponseEntity<?> createUser(@RequestBody User user) {
    // Requirements fields
    if (user.getName() == null || user.getName().isEmpty()) {
        return ResponseEntity.badRequest().body("Name must not be null or empty");
    }
    if (user.getUsername() == null || user.getUsername().isEmpty()) {
        return ResponseEntity.badRequest().body("Username must not be null or empty");
    }
    
    // Email validation
    if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        return ResponseEntity.badRequest().body("Invalid email format");
    }
    if (user.getEmail() == null || user.getEmail().isEmpty()) {
        return ResponseEntity.badRequest().body("Email must not be null or empty");
    }
    
    // Password validation
    if (user.getPassword() == null || user.getPassword().isEmpty()) {
        return ResponseEntity.badRequest().body("Password must not be null or empty");
    }
    if (user.getPassword().length() < 8) {
        return ResponseEntity.badRequest().body("Password must be at least 8 characters long");
    }
    if (user.getPassword().length() > 30) {
        return ResponseEntity.badRequest().body("Password max lenght 30 characters long");
    }

    // Hashing password before save
    String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
    user.setPassword(hashedPassword);


    // Если все проверки пройдены, сохранить пользователя
    User createdUser = userRepository.save(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
}

    @GetMapping("/find/{username}")
    public User findUser(@PathVariable String username) {
        return userRepository.findByUsername(username);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
