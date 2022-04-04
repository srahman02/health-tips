package com.sojibur.healthtips.repository;

import com.sojibur.healthtips.model.HealthTips;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthTipsRepository extends MongoRepository<HealthTips, String> {
}
