package com.alkemy.disney_AlkemyChallenge.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
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

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Column(nullable = false)
    private int calificacion;

    @ManyToOne
    @JoinColumn(name = "id_genero", nullable = false)
    private GeneroEntity genero;

    @ManyToMany(mappedBy = "audiovisuales")
    private Set<PersonajeEntity> personajes;

    public AudiovisualEntity(String titulo, String imagen, int calififacion, GeneroEntity genero) {
        this.titulo = titulo;
        this.imagen = imagen;
        this.calificacion = calififacion;
        this.genero = genero;
    }
}
