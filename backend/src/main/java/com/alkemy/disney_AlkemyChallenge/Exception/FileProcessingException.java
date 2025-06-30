package com.alkemy.disney_AlkemyChallenge.Exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Excepción que se lanza cuando ocurre un error al procesar un archivo")
public class FileProcessingException extends RuntimeException {
    public FileProcessingException(String message) {
        super(message);
    }
}
