package com.alkemy.disney_AlkemyChallenge.DTO.Genero;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneroDTO {
    @NotBlank(message = "El nombre del género no puede estar en blanco")
    private String nombre;
    private MultipartFile imagen;
}
