package com.alkemy.disney_AlkemyChallenge.DTO.Genero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneroResponseDTO {
    private Long id;
    private String nombre;
    private String imagen;
}
