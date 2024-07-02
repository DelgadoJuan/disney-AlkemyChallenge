package com.alkemy.disney_AlkemyChallenge.Service;

import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeListDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;

import java.util.List;
import java.util.Optional;

public interface IPersonajeService {
    List<PersonajeListDTO> charactersList();
    Optional<PersonajeEntity> getCharacter(Long id);
    List<PersonajeEntity> getCharactersByName(String characterName);
    List<PersonajeEntity> getCharactersByAge(int age);
    List<PersonajeEntity> getCharactersByWeight(double weight);
    List<PersonajeEntity> getCharactersByAudiovisual(Long audiovisualId);
    boolean addCharacter(PersonajeEntity character);
    boolean removeCharacter(Long id);
    boolean updateCharacter(Long id, PersonajeEntity character);
}
