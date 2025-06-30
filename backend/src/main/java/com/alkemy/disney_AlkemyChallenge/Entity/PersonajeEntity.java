package com.alkemy.disney_AlkemyChallenge.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "personaje")
@Schema(description = "Entidad que representa un personaje en el sistema")
public class PersonajeEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Schema(description = "Identificador único del personaje", example = "1")
    private Long id;

    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    @Column(nullable = false)
    @Schema(description = "Nombre del personaje", example = "El Rey León", required = true)
    private String nombre;

    @NotNull(message = "La edad no puede ser nula")
    @Positive(message = "La edad debe ser un número positivo")
    @Column(nullable = false)
    @Schema(description = "Edad del personaje", example = "30", required = true)
    private int edad;

    @NotNull(message = "El peso no puede ser nulo")
    @Positive(message = "El peso debe ser un número positivo")
    @Column(nullable = false)
    @Schema(description = "Peso del personaje", example = "75.5", required = true)
    private double peso;

    @Size(max = 255, message = "La URL de la imagen no puede tener más de 255 caracteres")
    @Column(name = "imagen", length = 255)
    @Schema(description = "URL de la imagen del personaje")
    private String imagen;

    @NotBlank(message = "La historia no puede estar en blanco")
    @Size(min = 1, max = 1000, message = "La historia debe tener entre 1 y 1000 caracteres")
    @Column(nullable = false)
    @Schema(description = "Historia del personaje", example = "Un joven león debe enfrentar su destino...", required = true)
    private String historia;

    @ManyToMany
    @JoinTable(
            name = "personaje_audiovisual",
            joinColumns = @JoinColumn(name = "id_personaje"),
            inverseJoinColumns = @JoinColumn(name = "id_audiovisual")
    )
    @Schema(description = "Audiovisuals asociados a este personaje")
    private Set<AudiovisualEntity> audiovisuales = new HashSet<>();

    public PersonajeEntity(String nombre, int edad, double peso, String imagen, String historia) {
        this.nombre = nombre;
        this.edad = edad;
        this.peso = peso;
        this.imagen = imagen;
        this.historia = historia;
    }

    public void addAudiovisual(AudiovisualEntity audiovisual) {
        audiovisuales.add(audiovisual);
    }

    public void removeAudiovisual(AudiovisualEntity audiovisual) {
        audiovisuales.remove(audiovisual);
    }
}
