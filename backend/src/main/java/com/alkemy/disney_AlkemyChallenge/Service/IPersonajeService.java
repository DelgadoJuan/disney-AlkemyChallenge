package com.alkemy.disney_AlkemyChallenge.Service;

import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeListDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajePrintDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;

import java.util.List;
import java.util.Optional;

public interface IPersonajeService {
    List<PersonajeListDTO> charactersList();
    List<PersonajeListDTO> getTopCharacters(int limit);
    Optional<PersonajePrintDTO> getCharacter(Long id);
    List<PersonajePrintDTO> getAllCharacters();
    /*List<PersonajeEntity> getCharactersByName(String characterName);
    List<PersonajeEntity> getCharactersByAge(int age);
    List<PersonajeEntity> getCharactersByWeight(double weight); */
    List<PersonajeListDTO> getCharactersByAudiovisual(Long audiovisualId);
    boolean addCharacter(PersonajeDTO personajeDTO);
    boolean removeCharacter(Long id);
    boolean updateCharacter(Long id, PersonajeDTO personajeDTO);
}
