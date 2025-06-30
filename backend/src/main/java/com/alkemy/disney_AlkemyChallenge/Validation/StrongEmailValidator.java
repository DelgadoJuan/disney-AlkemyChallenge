package com.alkemy.disney_AlkemyChallenge.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class StrongEmailValidator implements ConstraintValidator<StrongEmail, String> {
    
    // Regex que valida:
    // - Comienza con letras, números o caracteres especiales permitidos
    // - Debe contener un @
    // - Después del @ debe tener un dominio válido
    // - El dominio debe tener al menos un punto
    // - No permite caracteres especiales peligrosos
    // - Longitud máxima de 254 caracteres (estándar RFC)
    private static final String EMAIL_PATTERN = 
        "^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
        "(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public void initialize(StrongEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return pattern.matcher(email).matches();
    }
} 