package com.sojibur.healthtips.service.impl;

import com.sojibur.healthtips.exception.InternalServerException;
import com.sojibur.healthtips.exception.TipsNotFoundException;
import com.sojibur.healthtips.model.HealthTips;
import com.sojibur.healthtips.repository.HealthTipsRepository;
import com.sojibur.healthtips.service.HealthTipsService;
import org.springframework.stereotype.Service;

@Service
public class HealthTipsServiceImpl implements HealthTipsService {

    private final HealthTipsRepository healthTipsRepository;

    public HealthTipsServiceImpl(HealthTipsRepository healthTipsRepository){
        this.healthTipsRepository = healthTipsRepository;
    }

    @Override
    public HealthTips getHealthTipsById(String id) {
        return healthTipsRepository.findById(id)
                .orElseThrow(()-> new TipsNotFoundException("USER_NOT_FOUND_FOR_THE_GIVEN_ID: " + id));
    }

    @Override
    public HealthTips createTips(HealthTips tips) {
        try{
            return healthTipsRepository.save(tips);
        }
        catch (Exception ex){
            throw new InternalServerException("INTERNAL_SERVER_ERROR");
        }
    }
}
