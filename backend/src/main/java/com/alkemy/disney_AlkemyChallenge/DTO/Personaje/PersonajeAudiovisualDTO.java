package com.alkemy.disney_AlkemyChallenge.DTO.Personaje;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonajeAudiovisualDTO {
    private Long id;
    private String titulo;
    private String imagen;
    private LocalDate fechaCreacion;
    private double calificacion;
}
