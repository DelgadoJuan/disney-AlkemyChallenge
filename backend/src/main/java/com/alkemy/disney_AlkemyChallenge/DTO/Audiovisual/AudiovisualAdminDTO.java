package com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudiovisualAdminDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagen;
    private String director;
    private LocalDate fechaCreacion;
    private int duracion;
    private double calificacion;
    private String nombreGenero;
} 