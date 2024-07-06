package com.alkemy.disney_AlkemyChallenge.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
public class AudiovisualEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar en blanco")
    @Size(min = 1, max = 100, message = "El título debe tener entre 1 y 100 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "La imagen no puede estar en blanco")
    @Size(max = 255, message = "La URL de la imagen no puede tener más de 255 caracteres")
    @Column(nullable = false)
    private String imagen;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Column(nullable = false)
    private int calificacion;

    @ManyToOne
    @JoinColumn(name = "id_genero")
    @JsonBackReference
    private GeneroEntity genero;

    @ManyToMany(mappedBy = "audiovisuales")
    @JsonIgnore
    private Set<PersonajeEntity> personajes;

    public AudiovisualEntity(String titulo, String imagen, int calififacion, GeneroEntity genero) {
        this.titulo = titulo;
        this.imagen = imagen;
        this.calificacion = calififacion;
        this.genero = genero;
        this.personajes = new HashSet<>();
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
