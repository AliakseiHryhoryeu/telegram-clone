package com.example.telegramclone.controllers;

import java.time.LocalDateTime;

import com.example.telegramclone.DTO.User.UserCreateRequest;
import com.example.telegramclone.models.JwtPayload;
import com.example.telegramclone.models.User;
import com.example.telegramclone.repositories.UserRepository;
import com.example.telegramclone.utils.PasswordUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;

import com.example.telegramclone.DTO.User.UserBlockUserDTO;

import jakarta.validation.Valid;

import com.example.telegramclone.utils.JwtUtil;

import com.example.telegramclone.DTO.User.UserChangeDescriptionDTO;
import com.example.telegramclone.DTO.User.UserChangeFirstnameDTO;
import com.example.telegramclone.DTO.User.UserChangeLastnameDTO;
import com.example.telegramclone.DTO.User.UserChangePasswordDTO;
import com.example.telegramclone.DTO.User.UserChangeUsernameDTO;
import com.example.telegramclone.DTO.User.UserCreateResponse;
import static com.example.telegramclone.utils.PasswordUtil.checkPassword;
import static com.example.telegramclone.utils.PasswordUtil.hashPassword;

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

    // DELETE IT LATER
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
    public ResponseEntity<?> changeUsername(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid UserChangeUsernameDTO req) {

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
        // Find user for id and email
        Optional<User> userOpt = userRepository.findById(jwtPayload.getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOpt.get();

        // new Username validation
        if (req.getNewusername() == null || req.getNewusername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username must not be null or empty");
        }
        if (req.getNewusername().length() < 3 || req.getNewusername().length() > 35) {
            return ResponseEntity.badRequest().body("Username must be between 3 and 35 characters long");
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

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid UserChangePasswordDTO req) {

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
        // Find user for id and email
        Optional<User> userOpt = userRepository.findById(jwtPayload.getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOpt.get();

        // Password validation
        if (req.getNewPassword() == null || req.getNewPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password must not be null or empty");
        }
        if (req.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters long");
        }
        if (req.getNewPassword().length() > 40) {
            return ResponseEntity.badRequest().body("Password max lenght 40 characters long");
        }

        // Old Password verification
        if (!checkPassword(req.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Wrong old password");
        }
        String newHashedPassword = hashPassword(req.getNewPassword());
        // Refresh username and save
        user.setPassword(newHashedPassword);
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/changeDescription")
    public ResponseEntity<?> changePassword(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid UserChangeDescriptionDTO req) {

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
        // Find user for id
        Optional<User> userOpt = userRepository.findById(jwtPayload.getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOpt.get();

        // Validation new Description
        if (req.getNewDescription().length() > 2000) {
            return ResponseEntity.badRequest().body("Maximum description size = 2000 letters");
        }

        // Refresh description and save
        user.setDescription(req.getNewDescription());
        userRepository.save(user);

        return ResponseEntity.ok("Description updated successfully");
    }

    @PostMapping("/changeFirstname")
    public ResponseEntity<?> changeFirstname(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid UserChangeFirstnameDTO req) {

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
        // Find user for id
        Optional<User> userOpt = userRepository.findById(jwtPayload.getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOpt.get();

        // Validation new Firstname
        if (req.getFirstname().length() > 30) {
            return ResponseEntity.badRequest().body("Maximum firstname size = 30 letters");
        }

        // Refresh description and save
        user.setFirstname(req.getFirstname());
        userRepository.save(user);

        return ResponseEntity.ok("Firstname updated successfully");
    }

    @PostMapping("/changeLastname")
    public ResponseEntity<?> changeLastname(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid UserChangeLastnameDTO req) {

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
        // Find user for id
        Optional<User> userOpt = userRepository.findById(jwtPayload.getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOpt.get();

        // Validation new Firstname
        if (req.getLastname().length() > 30) {
            return ResponseEntity.badRequest().body("Maximum Lastname size = 30 letters");
        }

        // Refresh description and save
        user.setLastname(req.getLastname());
        userRepository.save(user);

        return ResponseEntity.ok("Lastname updated successfully");
    }

    @PostMapping("/changeLastSeen")
    public ResponseEntity<?> changeLastSeen(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

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
        // Find user for id
        Optional<User> userOpt = userRepository.findById(jwtPayload.getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOpt.get();

        // Refresh description and save
        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);

        return ResponseEntity.ok("Lastname updated successfully");
    }

    @PostMapping("/BlockUser")
    public ResponseEntity<?> changeLastname(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid UserBlockUserDTO req) {

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
        // Find user for id
        Optional<User> userOpt = userRepository.findById(jwtPayload.getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOpt.get();

        // Validation blocked user is real?
        if (userRepository.findById(req.getId()).isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Refresh description and save

        user.addBlocked(req.getId());
        userRepository.save(user);

        return ResponseEntity.ok("Blocked list updated successfully");
    }

    @GetMapping("/searchUsers")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String searchTerm) {
        // Поиск пользователей по имени или имени пользователя
        List<User> users = userRepository.searchByUsername(searchTerm);

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
