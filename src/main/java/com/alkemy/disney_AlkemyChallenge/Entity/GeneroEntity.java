package com.alkemy.disney_AlkemyChallenge.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "genero")
public class GeneroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGenero;

    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La imagen no puede estar en blanco")
    @Size(max = 255, message = "La URL de la imagen no puede tener más de 255 caracteres")
    @Column(nullable = false)
    private String imagen;

    @OneToMany(mappedBy = "genero")
    private Set<AudiovisualEntity> audiovisuales;

    public GeneroEntity(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }
}
