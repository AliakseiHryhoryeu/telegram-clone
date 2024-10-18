package com.example.telegramclone.repositories;

import java.util.List;
import java.util.Optional;

import com.example.telegramclone.models.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("{ '$or': [ { 'firstname': { '$regex': ?0, '$options': 'i' } }, { 'lastname': { '$regex': ?0, '$options': 'i' } }, { 'username': { '$regex': ?0, '$options': 'i' } } ] }")
    List<User> searchByNameOrUsername(String searchTerm);
}
