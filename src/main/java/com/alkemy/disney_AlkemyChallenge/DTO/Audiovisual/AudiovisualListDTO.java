package com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudiovisualListDTO {
    private String imagen;
    private String titulo;
    private LocalDate fechaCreacion;
}
