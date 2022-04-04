package com.sojibur.healthtips.controller;

import com.sojibur.healthtips.model.HealthTips;
import com.sojibur.healthtips.service.HealthTipsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthTipsController {

    private final HealthTipsService healthTipsService;


    public HealthTipsController(HealthTipsService healthTipsService) {

        this.healthTipsService = healthTipsService;
    }

    @GetMapping("/tips/{id}")
    public ResponseEntity<HealthTips> getHealthTips(@PathVariable String id){
        return ResponseEntity.ok().body(healthTipsService.getHealthTipsById(id));
    }

    @PostMapping("/tips")
    public ResponseEntity<HealthTips> createUser(@RequestBody HealthTips tips){
        return new ResponseEntity<>(healthTipsService.createTips(tips), HttpStatus.CREATED);
    }
}
