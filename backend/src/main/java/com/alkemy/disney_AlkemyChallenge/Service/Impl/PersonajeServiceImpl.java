package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeListDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajePrintDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;
import com.alkemy.disney_AlkemyChallenge.Exception.ResourceNotFoundException;
import com.alkemy.disney_AlkemyChallenge.Mapper.PersonajeMapper;
import com.alkemy.disney_AlkemyChallenge.Repository.PersonajeRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IPersonajeService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PersonajeServiceImpl implements IPersonajeService {
    private final PersonajeRepository personajeRepository;
    private final String directory = "src/main/resources/static/images/personaje/";
    private final PersonajeMapper personajeMapper;

    /** 
     * Obtiene todos los personajes
     * @return Lista de DTOs de personajes
     * **/
    @Override
    public List<PersonajeListDTO> charactersList() {
        return personajeRepository.findAll().stream()
                .map(personajeMapper::personajeEntityToPersonajeListDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonajePrintDTO> getAllCharacters() {
        return personajeRepository.findAll().stream()
                .map(personajeMapper::personajeEntityToPersonajePrintDTO)
                .collect(Collectors.toList());
    }

    /** 
     * Obtiene un personaje por su ID
     * @param id ID del personaje
     * @return DTO de personaje
     * **/
    @Override
    public Optional<PersonajePrintDTO> getCharacter(Long id) {
        return personajeRepository.findById(id).map(personajeMapper::personajeEntityToPersonajePrintDTO);
    }

    /** 
     * Obtiene todos los personajes de un audiovisual
     * @param audiovisualId ID del audiovisual
     * @return Lista de DTOs de personajes
     * **/
    @Override
    public List<PersonajeListDTO> getCharactersByAudiovisual(Long audiovisualId) {
        return personajeRepository.findByAudiovisuales(audiovisualId)
                .stream()
                .map(personajeMapper::personajeEntityToPersonajeListDTO)
                .collect(Collectors.toList());
    }

    /** 
     * Agrega un personaje
     * @param personajeDTO DTO de personaje a agregar
     * @return true si el personaje se agregó correctamente, false en caso contrario
     * **/
    @Override
    public boolean addCharacter(PersonajeDTO personajeDTO) {
        try {
            PersonajeEntity personajeEntity = personajeMapper.personajeDTOtoPersonajeEntity(personajeDTO);

            String fileName = personajeDTO.getImagen().getOriginalFilename();
            Path path = Paths.get(directory + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(personajeDTO.getImagen().getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

            personajeEntity.setImagen("images/personaje/" + fileName);

            personajeRepository.save(personajeEntity);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /** 
     * Elimina un personaje
     * @param id ID del personaje a eliminar
     * @return true si el personaje se eliminó correctamente, false en caso contrario
     * **/
    @Override
    public boolean removeCharacter(Long id) {
        try {
            PersonajeEntity personaje = personajeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Personaje no encontrado"));
            // Eliminar la imagen del personaje si existe
            String imagePath = personaje.getImagen();
            if (imagePath != null) {
                // Convertir la ruta de la base de datos a la ruta del sistema de archivos
                String filePath = directory + imagePath.replace("images/personaje/", "");
                File imageFile = new File(filePath);
                if (imageFile.exists()) {
                    imageFile.delete(); // Borrar la imagen del sistema de archivos
                }
            }

            // Eliminar el personaje de la base de datos
            personajeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** 
     * Actualiza un personaje
     * @param id ID del personaje a actualizar
     * @param personajeDTO DTO de personaje a actualizar
     * @return true si el personaje se actualizó correctamente, false en caso contrario
     * **/
    @Override
    public boolean updateCharacter(Long id, PersonajeDTO personajeDTO) {
        try {
            PersonajeEntity currentCharacter = personajeRepository.findById(id).orElseThrow(() -> new Exception("Personaje no encontrado"));
            currentCharacter.setNombre(personajeDTO.getNombre());
            currentCharacter.setEdad(personajeDTO.getEdad());
            currentCharacter.setPeso(personajeDTO.getPeso());
            currentCharacter.setHistoria(personajeDTO.getHistoria());

            if (personajeDTO.getImagen() != null && personajeDTO.getImagen().getOriginalFilename() != null) {
                // Eliminar la imagen anterior si existe
                String oldImagePath = currentCharacter.getImagen();
                if (oldImagePath != null) {
                    // Convertir la ruta de la base de datos a la ruta del sistema de archivos
                    String oldFilePath = directory + oldImagePath.replace("images/personaje/", "");
                    File oldImageFile = new File(oldFilePath);
                    if (oldImageFile.exists()) {
                        oldImageFile.delete(); // Borrar la imagen anterior
                    }
                }

                String fileName = personajeDTO.getImagen().getOriginalFilename();
                Path path = Paths.get(directory + fileName);
                Files.createDirectories(path.getParent());
                Files.copy(personajeDTO.getImagen().getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                currentCharacter.setImagen("images/personaje/" + fileName);
            }

            personajeRepository.save(currentCharacter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<PersonajeListDTO> getTopCharacters(int limit) {
        return personajeRepository.findAll().stream()
            .map(personajeMapper::personajeEntityToPersonajeListDTO)
            .limit(limit)
            .collect(Collectors.toList());
    }

    /*@Override
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
    } */
}
