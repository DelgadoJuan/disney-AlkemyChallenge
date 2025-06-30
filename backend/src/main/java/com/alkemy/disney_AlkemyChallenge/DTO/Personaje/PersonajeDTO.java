package com.alkemy.disney_AlkemyChallenge.DTO.Personaje;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonajeDTO {
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    private String nombre;

    @NotNull(message = "La edad no puede ser nula")
    @Positive(message = "La edad debe ser un número positivo")
    private int edad;

    @NotNull(message = "El peso no puede ser nulo")
    @Positive(message = "El peso debe ser un número positivo")
    private double peso;

    @NotBlank(message = "La historia no puede estar en blanco")
    @Size(min = 1, max = 1000, message = "La historia debe tener entre 1 y 1000 caracteres")
    private String historia;

    @Size(max = 255, message = "La URL de la imagen no puede tener más de 255 caracteres")
    private MultipartFile imagen;
}
