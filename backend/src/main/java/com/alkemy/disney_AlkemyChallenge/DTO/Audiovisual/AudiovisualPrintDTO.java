package com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual;

import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeListDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudiovisualPrintDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagen;
    private String director;
    private LocalDate fechaCreacion;
    private int duracion;
    private double calificacion;
    private String nombreGenero;
    private List<PersonajeListDTO> personajes;

    public AudiovisualPrintDTO(Long id, String titulo, String descripcion, String imagen, String director, LocalDate fechaCreacion, int duracion, double calificacion, String nombreGenero) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.director = director;
        this.fechaCreacion = fechaCreacion;
        this.duracion = duracion;
        this.calificacion = calificacion;
        this.nombreGenero = nombreGenero;
    }
}
