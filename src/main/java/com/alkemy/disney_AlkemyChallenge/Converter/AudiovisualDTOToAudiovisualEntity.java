package com.alkemy.disney_AlkemyChallenge.Converter;

import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AudiovisualDTOToAudiovisualEntity implements Converter<AudiovisualDTO, AudiovisualEntity> {
    private final GeneroRepository generoRepository;

    @Autowired
    public AudiovisualDTOToAudiovisualEntity(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @Override
    public AudiovisualEntity convert(AudiovisualDTO audiovisualDTO) {
        AudiovisualEntity audiovisualEntity = new AudiovisualEntity();
        audiovisualEntity.setTitulo(audiovisualDTO.getTitulo());
        audiovisualEntity.setCalificacion(audiovisualDTO.getCalificacion());
        audiovisualEntity.setFechaCreacion(audiovisualDTO.getFechaCreacion());
        GeneroEntity genero = generoRepository.findById(audiovisualDTO.getGeneroId()).orElseThrow();
        audiovisualEntity.setGenero(genero);
        genero.addAudiovisual(audiovisualEntity);
        return audiovisualEntity;
    }
}
