package com.alkemy.disney_AlkemyChallenge.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "genero")
public class GeneroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La imagen no puede estar en blanco")
    @Size(max = 255, message = "La URL de la imagen no puede tener más de 255 caracteres")
    @Column(nullable = false)
    private String imagen;

    @OneToMany(mappedBy = "genero")
    @JsonManagedReference
    private Set<AudiovisualEntity> audiovisuales = new HashSet<>();

    public GeneroEntity(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public void addAudiovisual(AudiovisualEntity audiovisual) {
        this.audiovisuales.add(audiovisual);
    }

    public void removeAudiovisual(AudiovisualEntity audiovisualEntity) {
        this.audiovisuales.remove(audiovisualEntity);
        audiovisualEntity.setGenero(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneroEntity that = (GeneroEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre) && Objects.equals(imagen, that.imagen) && Objects.equals(audiovisuales, that.audiovisuales);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, imagen);
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
