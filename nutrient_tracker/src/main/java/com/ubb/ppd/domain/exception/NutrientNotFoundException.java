package com.ubb.ppd.domain.exception;

public class NutrientNotFoundException extends RuntimeException {
    public NutrientNotFoundException(String message) {
        super(message);
    }
}
