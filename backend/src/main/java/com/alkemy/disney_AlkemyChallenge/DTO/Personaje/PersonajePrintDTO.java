package com.alkemy.disney_AlkemyChallenge.DTO.Personaje;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonajePrintDTO {
    private Long id;
    private String nombre;
    private int edad;
    private double peso;
    private String historia;
    private String imagen;
    private List<PersonajeAudiovisualDTO> audiovisuales;
}
