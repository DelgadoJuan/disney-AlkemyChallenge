package com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AudiovisualListDTO {
    private Long id;
    private String titulo;
    private String imagen;
    private int duracion;
    private LocalDate fechaCreacion;
    private double calificacion;
    private String nombreGenero;
}
