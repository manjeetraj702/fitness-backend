package com.fittrack.analytics_core.repository;

import com.fittrack.analytics_core.model.UserProfileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfileDocument, String> {
    // Fetches all past health predictions for a specific user to build a chart
    List<UserProfileDocument> findByUserIdOrderByTimestampDesc(String userId);
}