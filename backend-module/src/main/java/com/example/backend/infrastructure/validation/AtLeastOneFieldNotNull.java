package com.example.backend.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import com.seu.projeto.infrastructure.validation.AtLeastOneFieldValidator;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneFieldValidator.class)
@Documented
public @interface AtLeastOneFieldNotNull {
    String message() default "Pelo menos um campo deve ser informado para atualização";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
