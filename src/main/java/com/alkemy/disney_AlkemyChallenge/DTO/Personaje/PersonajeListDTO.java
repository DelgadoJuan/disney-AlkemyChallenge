package com.alkemy.disney_AlkemyChallenge.DTO.Personaje;

import lombok.Data;

@Data
public class PersonajeListDTO {
    private String nombre;
    private String imagen;

    public PersonajeListDTO(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }
}
