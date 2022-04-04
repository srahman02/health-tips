package com.sojibur.healthtips.service;

import com.sojibur.healthtips.model.HealthTips;

public interface HealthTipsService {
    HealthTips getHealthTipsById(String id);
    HealthTips createTips(HealthTips tips);
}
