package com.fittrack.analytics_core.repository;

import com.fittrack.analytics_core.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Spring automatically translates this into a MongoDB query: 
    // db.users.find({ "email": email })
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
}