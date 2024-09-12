package com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudiovisualDTO {
    @NotBlank(message = "El título no puede estar en blanco")
    @Size(min = 1, max = 100, message = "El título debe tener entre 1 y 100 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "La imagen no puede estar en blanco")
    @Size(max = 255, message = "La URL de la imagen no puede tener más de 255 caracteres")
    private MultipartFile imagen;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Column(nullable = false)
    private int calificacion;

    @Positive(message = "El id debe ser un número positivo")
    private Long generoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudiovisualDTO that = (AudiovisualDTO) o;
        return calificacion == that.calificacion && Objects.equals(titulo, that.titulo) && Objects.equals(imagen, that.imagen) && Objects.equals(fechaCreacion, that.fechaCreacion) && Objects.equals(generoId, that.generoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, imagen, fechaCreacion, calificacion, generoId);
    }
}
