package com.alkemy.disney_AlkemyChallenge.Service;

import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualListDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualPrintDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;

import java.util.List;

public interface IAudiovisualService {
    List<AudiovisualListDTO> getAudiovisuals();
    List<AudiovisualPrintDTO> getAudiovisualsByOrder(String order);
    List<AudiovisualPrintDTO> getAudiovisualsByGenre(Long genreId);
    List<AudiovisualPrintDTO> getAudiovisualsByTitle(String audiovisualTitle);
    AudiovisualEntity getAudiovisual(Long id);
    boolean addAudiovisual(AudiovisualDTO audiovisual);
    boolean deleteAudiovisual(Long audiovisualId);
    boolean updateAudiovisual(Long id, AudiovisualDTO audiovisual);
    boolean addCharacter(Long id, Long personajeId);
    boolean removeCharacter(Long id, Long personajeId);
    boolean updateGenre(Long id, Long genreId);
}
