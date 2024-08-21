package com.alkemy.disney_AlkemyChallenge.Service;

import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;

import java.util.List;

public interface IGeneroService {
    boolean addGenero(GeneroDTO generoDTO);
    List<GeneroEntity> getGeneros();
    boolean updateGenero(Long id, GeneroDTO generoDTO);
    boolean deleteGenero(Long id);
    boolean deleteAudiovisualFromGenre(Long id, Long audiovisualId);
}
