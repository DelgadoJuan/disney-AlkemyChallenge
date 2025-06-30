package com.alkemy.disney_AlkemyChallenge.Exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Excepción que se lanza cuando una contraseña no es válida")
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
