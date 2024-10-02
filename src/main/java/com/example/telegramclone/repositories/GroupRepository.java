package com.example.telegramclone.repositories;

import com.example.telegramclone.models.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
    Group findByGroupName(String groupName);
}
