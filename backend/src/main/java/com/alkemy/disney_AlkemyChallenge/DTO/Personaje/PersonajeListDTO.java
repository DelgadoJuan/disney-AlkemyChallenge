package com.alkemy.disney_AlkemyChallenge.DTO.Personaje;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonajeListDTO {
    private Long id;
    private String nombre;
    private String imagen;
    private int edad;
    private double peso;
}
