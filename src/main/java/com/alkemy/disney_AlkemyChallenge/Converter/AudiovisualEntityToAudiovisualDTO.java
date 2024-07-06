package com.alkemy.disney_AlkemyChallenge.Converter;

import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualPrintDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AudiovisualEntityToAudiovisualDTO implements Converter<AudiovisualEntity, AudiovisualPrintDTO> {
    @Override
    public AudiovisualPrintDTO convert(AudiovisualEntity audiovisualEntity) {
        AudiovisualPrintDTO audiovisualPrintDTO = new AudiovisualPrintDTO();
        audiovisualPrintDTO.setTitulo(audiovisualEntity.getTitulo());
        audiovisualPrintDTO.setFechaCreacion(audiovisualEntity.getFechaCreacion());
        audiovisualPrintDTO.setCalificacion(audiovisualEntity.getCalificacion());
        audiovisualPrintDTO.setImagen(audiovisualEntity.getImagen());
        audiovisualPrintDTO.setNombreGenero(audiovisualEntity.getGenero().toString());
        return audiovisualPrintDTO;
    }
}
