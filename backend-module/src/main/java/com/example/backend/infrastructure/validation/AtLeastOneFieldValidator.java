package com.seu.projeto.infrastructure.validation;

import com.example.backend.infrastructure.validation.AtLeastOneFieldNotNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class AtLeastOneFieldValidator implements ConstraintValidator<AtLeastOneFieldNotNull, Object> {

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return false;
        }

        return Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> !field.getName().equals("serialVersionUID")) // ignora campos estáticos
                .peek(field -> field.setAccessible(true))
                .anyMatch(field -> {
                    try {
                        return field.get(object) != null;
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                });
    }
}