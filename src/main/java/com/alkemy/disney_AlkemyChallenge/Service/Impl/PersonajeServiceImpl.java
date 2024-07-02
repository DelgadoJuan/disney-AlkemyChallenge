package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeListDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.PersonajeRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IPersonajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonajeServiceImpl implements IPersonajeService {

    private final PersonajeRepository personajeRepository;

    @Autowired
    public PersonajeServiceImpl(PersonajeRepository personajeRepository) {
        this.personajeRepository = personajeRepository;
    }

    // GET
    @Override
    public List<PersonajeListDTO> charactersList() {
        return personajeRepository.findAll().stream()
                .map(personaje -> new PersonajeListDTO(personaje.getNombre(), personaje.getImagen()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PersonajeEntity> getCharacter(Long id) {
        return personajeRepository.findById(id);
    }

    @Override
    public List<PersonajeEntity> getCharactersByName(String characterName) {
        return personajeRepository.findByNombre(characterName);
    }

    @Override
    public List<PersonajeEntity> getCharactersByAge(int age) {
        return personajeRepository.findByEdad(age);
    }

    @Override
    public List<PersonajeEntity> getCharactersByWeight(double weight) {
        return personajeRepository.findByPeso(weight);
    }

    @Override
    public List<PersonajeEntity> getCharactersByAudiovisual(Long audiovisualId) {
        return personajeRepository.findByAudiovisuales(audiovisualId);
    }


    // POST
    @Override
    public boolean addCharacter(PersonajeEntity character) {
        try {
            personajeRepository.save(character);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // DELETE
    @Override
    public boolean removeCharacter(Long id) {
        try {
            personajeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // PUT
    @Override
    public boolean updateCharacter(Long id, PersonajeEntity character) {
        PersonajeEntity currentCharacter = null;
        try {
            currentCharacter = personajeRepository.findById(id).orElseThrow(() -> new Exception("Personaje no encontrado"));
            currentCharacter.setNombre(character.getNombre());
            currentCharacter.setImagen(character.getImagen());
            currentCharacter.setEdad(character.getEdad());
            currentCharacter.setPeso(character.getPeso());
            currentCharacter.setHistoria(character.getHistoria());
            personajeRepository.save(currentCharacter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
