package com.alkemy.disney_AlkemyChallenge.DTO.Personaje;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonajeDTO {
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotNull(message = "La edad no puede ser nula")
    @Positive(message = "La edad debe ser un número positivo")
    @Column(nullable = false)
    private int edad;

    @NotNull(message = "El peso no puede ser nulo")
    @Positive(message = "El peso debe ser un número positivo")
    @Column(nullable = false)
    private double peso;

    @NotBlank(message = "La historia no puede estar en blanco")
    @Size(min = 1, max = 1000, message = "La historia debe tener entre 1 y 1000 caracteres")
    @Column(nullable = false)
    private String historia;

    @NotBlank(message = "La imagen no puede estar en blanco")
    @Size(max = 255, message = "La URL de la imagen no puede tener más de 255 caracteres")
    @Column(nullable = false)
    private MultipartFile imagen;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonajeDTO that = (PersonajeDTO) o;
        return edad == that.edad &&
                Double.compare(that.peso, peso) == 0 &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(historia, that.historia) &&
                Objects.equals(imagen, that.imagen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, edad, peso, historia, imagen);
    }
}
