package com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear o actualizar un audiovisual")
public class AudiovisualDTO {
    @Schema(description = "Título del audiovisual", example = "El Rey León", required = true)
    @NotBlank(message = "El título no puede estar en blanco")
    @Size(min = 1, max = 100, message = "El título debe tener entre 1 y 100 caracteres")
    private String titulo;

    @Schema(description = "Imagen del audiovisual", required = true)
    @NotBlank(message = "La imagen no puede estar en blanco")
    @Size(max = 255, message = "La URL de la imagen no puede tener más de 255 caracteres")
    private MultipartFile imagen;

    @Schema(description = "Fecha de creación del audiovisual", example = "1994-06-24")
    private LocalDate fechaCreacion;

    @Schema(description = "Duración en minutos", example = "88", minimum = "1")
    @Min(value = 1, message = "La duración mínima es 1 minuto")
    private int duracion;

    @Schema(description = "Descripción del audiovisual", example = "Un joven león debe enfrentar su destino...", maxLength = 1000)
    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    @NotBlank(message = "La descripción no puede estar en blanco")
    private String descripcion;

    @Schema(description = "Calificación del audiovisual (0-5)", example = "4.5", minimum = "0", maximum = "5")
    @NotBlank(message = "La calificación no puede estar en blanco")
    @Min(value = 0, message = "La calificación no puede ser negativa")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Digits(integer = 1, fraction = 2, message = "La calificación debe tener solo dos decimales")
    private double calificacion;

    @Schema(description = "Director del audiovisual", example = "Roger Allers", required = true)
    @NotBlank(message = "El director no puede estar en blanco")
    private String director;

    @Schema(description = "ID del género al que pertenece el audiovisual", example = "1", minimum = "1")
    @Positive(message = "El id debe ser un número positivo")
    private Long generoId;
}
