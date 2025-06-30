package com.alkemy.disney_AlkemyChallenge.Exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Excepción que se lanza cuando un email ya está registrado")
public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
