package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.Converter.PersonajeDTOToPersonajeEntity;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeListDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.PersonajeRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IPersonajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonajeServiceImpl implements IPersonajeService {

    private final PersonajeRepository personajeRepository;
    private final PersonajeDTOToPersonajeEntity personajeDTOToPersonajeEntity;
    private final String directory = "src/main/resources/static/images/personaje/";

    @Autowired
    public PersonajeServiceImpl(PersonajeRepository personajeRepository, PersonajeDTOToPersonajeEntity personajeDTOToPersonajeEntity) {
        this.personajeRepository = personajeRepository;
        this.personajeDTOToPersonajeEntity = personajeDTOToPersonajeEntity;
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
    public boolean addCharacter(PersonajeDTO personajeDTO) {
        try {
            String fileName = personajeDTO.getImagen().getOriginalFilename();

            Path path = Paths.get(directory + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(personajeDTO.getImagen().getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

            PersonajeEntity personajeEntity = personajeDTOToPersonajeEntity.convert(personajeDTO);
            personajeEntity.setImagen("/images/personaje/" + fileName);

            personajeRepository.save(personajeEntity);
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
    public boolean updateCharacter(Long id, PersonajeDTO personajeDTO) {
        try {
            PersonajeEntity currentCharacter = personajeRepository.findById(id).orElseThrow(() -> new Exception("Personaje no encontrado"));
            currentCharacter.setNombre(personajeDTO.getNombre());
            currentCharacter.setEdad(personajeDTO.getEdad());
            currentCharacter.setPeso(personajeDTO.getPeso());
            currentCharacter.setHistoria(personajeDTO.getHistoria());

            // Eliminar la imagen anterior si existe
            String oldImagePath = currentCharacter.getImagen();
            if (oldImagePath != null) {
                File oldImageFile = new File(oldImagePath);
                if (oldImageFile.exists()) {
                    oldImageFile.delete(); // Borrar la imagen anterior
                }
            }

            String fileName = personajeDTO.getImagen().getOriginalFilename();
            Path path = Paths.get(directory + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(personajeDTO.getImagen().getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            currentCharacter.setImagen(directory + fileName);

            personajeRepository.save(currentCharacter);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
