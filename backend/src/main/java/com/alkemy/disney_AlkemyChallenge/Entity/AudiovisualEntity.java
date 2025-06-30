package com.alkemy.disney_AlkemyChallenge.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "audiovisual")
@Schema(description = "Entidad que representa un audiovisual en el sistema")
public class AudiovisualEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Schema(description = "Identificador único del audiovisual", example = "1")
    private Long id;

    @Schema(description = "Título del audiovisual", example = "El Rey León", required = true)
    @NotBlank(message = "El título no puede estar en blanco")
    @Size(min = 1, max = 100, message = "El título debe tener entre 1 y 100 caracteres")
    @Column(nullable = false)
    private String titulo;

    @Schema(description = "Director del audiovisual", example = "Roger Allers", required = true)
    @Column(name = "director", nullable = false)
    @NotBlank(message = "El director no puede estar en blanco")
    private String director;

    @Schema(description = "Descripción del audiovisual", example = "Un joven león debe enfrentar su destino...", maxLength = 1000)
    @Column(name = "descripcion")
    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    private String descripcion;

    @Schema(description = "Duración en minutos", example = "88", minimum = "1")
    @Column(name = "duracion", nullable = false)
    @Min(value = 1, message = "La duración mínima es 1 minuto")
    private int duracion;

    @Schema(description = "Ruta de la imagen del audiovisual", required = true)
    @NotBlank(message = "La imagen no puede estar en blanco")
    @Size(max = 255, message = "La URL de la imagen no puede tener más de 255 caracteres")
    @Column(nullable = false)
    private String imagen;

    @Schema(description = "Fecha de creación del audiovisual", example = "1994-06-24")
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Schema(description = "Calificación del audiovisual (0-5)", example = "4.5", minimum = "0", maximum = "5")
    @Column(name = "calificacion", nullable = false)
    @NotBlank(message = "La calificación no puede estar en blanco")
    @Min(value = 0, message = "La calificación no puede ser negativa")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Digits(integer = 1, fraction = 2, message = "La calificación debe tener solo dos decimales")
    private double calificacion;

    @Schema(description = "Género al que pertenece el audiovisual")
    @ManyToOne
    @JoinColumn(name = "id_genero")
    @JsonBackReference
    private GeneroEntity genero;

    @Schema(description = "Personajes que aparecen en el audiovisual")
    @ManyToMany(mappedBy = "audiovisuales")
    @JsonIgnore
    private Set<PersonajeEntity> personajes = new HashSet<>();

    public AudiovisualEntity(String titulo, String imagen, int calififacion, GeneroEntity genero) {
        this.titulo = titulo;
        this.imagen = imagen;
        this.calificacion = calififacion;
        this.genero = genero;
    }

    public AudiovisualEntity(String titulo, String imagen, LocalDate fechaCreacion, int calificacion,
                             GeneroEntity genero, Set<PersonajeEntity> personajes) {
        this.titulo = titulo;
        this.imagen = imagen;
        this.fechaCreacion = fechaCreacion;
        this.calificacion = calificacion;
        this.genero = genero;
        this.personajes = personajes;
    }

    public void addCharacter(PersonajeEntity personaje) {
        this.personajes.add(personaje);
    }

    public void removeCharacter(PersonajeEntity personaje) {
        this.personajes.remove(personaje);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudiovisualEntity that = (AudiovisualEntity) o;
        return calificacion == that.calificacion && Objects.equals(id, that.id) && Objects.equals(titulo, that.titulo) && Objects.equals(imagen, that.imagen) && Objects.equals(fechaCreacion, that.fechaCreacion) && Objects.equals(genero, that.genero) && Objects.equals(personajes, that.personajes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, imagen, fechaCreacion, calificacion);
    }
}
