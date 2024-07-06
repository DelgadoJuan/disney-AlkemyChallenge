package com.alkemy.disney_AlkemyChallenge.Service;

import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;

import java.util.List;

public interface IGeneroService {
    boolean addGenero(GeneroEntity generoEntity);
    List<GeneroEntity> getGeneros();
    boolean updateGenero(Long id, GeneroEntity generoEntity);
    boolean deleteGenero(Long id);
    boolean deleteAudiovisualFromGenre(Long id, Long audiovisualId);
}
