package com.example.telegramclone.repositories;

import java.util.List;
import java.util.Optional;

import com.example.telegramclone.models.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> searchByUsername(String username);
}
