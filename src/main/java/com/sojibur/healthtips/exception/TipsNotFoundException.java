package com.sojibur.healthtips.exception;

public class TipsNotFoundException extends RuntimeException{
    public TipsNotFoundException(String message){
        super(message);
    }
}
