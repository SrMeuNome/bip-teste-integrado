package com.example.backend.application.excepiton;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final String entityName;

    public EntityNotFoundException(String message, String entityName) {
        super(message);
        this.entityName = entityName;
    }
}
