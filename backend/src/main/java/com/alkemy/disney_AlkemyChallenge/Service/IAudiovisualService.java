package com.alkemy.disney_AlkemyChallenge.Service;

import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualListDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualPrintDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualAdminDTO;

import java.util.List;

public interface IAudiovisualService {
    List<AudiovisualListDTO> getAudiovisuals();
    List<AudiovisualAdminDTO> getAudiovisualsForAdmin();
    List<AudiovisualListDTO> getTopAudiovisuals(int limit);
    /*List<AudiovisualPrintDTO> getAudiovisualsByOrder(String order);
    List<AudiovisualPrintDTO> getAudiovisualsByGenre(Long genreId);
    List<AudiovisualPrintDTO> getAudiovisualsByTitle(String audiovisualTitle); */
    AudiovisualPrintDTO getAudiovisual(Long id);
    void addAudiovisual(AudiovisualDTO audiovisualDTO);
    void deleteAudiovisual(Long audiovisualId);
    void updateAudiovisual(Long id, AudiovisualDTO audiovisualDTO);
    void addCharacter(Long id, Long personajeId);
    void removeCharacter(Long id, Long personajeId);
    void updateGenre(Long id, Long genreId);
}
