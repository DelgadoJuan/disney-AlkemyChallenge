package com.alkemy.disney_AlkemyChallenge.Service;

import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroResponseDTO;

import java.util.List;

public interface IGeneroService {
    boolean addGenero(GeneroDTO generoDTO);
    List<GeneroResponseDTO> getGeneros();
    boolean updateGenero(Long id, GeneroDTO generoDTO);
    boolean deleteGenero(Long id);
    boolean deleteAudiovisualFromGenre(Long id, Long audiovisualId);
}
