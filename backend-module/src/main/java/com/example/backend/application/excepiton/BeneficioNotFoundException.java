package com.example.backend.application.excepiton;

public class BeneficioNotFoundException extends EntityNotFoundException {
    public BeneficioNotFoundException(String message) {
        super(message, "Beneficio");
    }
}
