package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualListDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualPrintDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualAdminDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;
import com.alkemy.disney_AlkemyChallenge.Exception.FileProcessingException;
import com.alkemy.disney_AlkemyChallenge.Exception.ResourceNotFoundException;
import com.alkemy.disney_AlkemyChallenge.Mapper.AudiovisualMapper;
import com.alkemy.disney_AlkemyChallenge.Mapper.PersonajeMapper;
import com.alkemy.disney_AlkemyChallenge.Repository.AudiovisualRepository;
import com.alkemy.disney_AlkemyChallenge.Repository.GeneroRepository;
import com.alkemy.disney_AlkemyChallenge.Repository.PersonajeRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IAudiovisualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AudiovisualServiceImpl implements IAudiovisualService {
    private final GeneroRepository generoRepository;
    private final PersonajeRepository personajeRepository;
    private final AudiovisualRepository audiovisualRepository;
    private final AudiovisualMapper audiovisualMapper;
    private final String directory = "src/main/resources/static/images/audiovisual/";
    private final PersonajeMapper personajeMapper;

    @Override
    /** 
     * Obtiene todos los audiovisuales con su género asociado
     * @return Lista de DTOs de audiovisuales con género
     * **/
    public List<AudiovisualListDTO> getAudiovisuals() {
        return audiovisualRepository.findAllWithGenero().stream()
                .map(audiovisualMapper::audiovisualEntityToAudiovisualListDTO)
                .collect(Collectors.toList());
    }

    @Override
    /** 
     * Obtiene todos los audiovisuales para la sección de administración
     * Incluye director y descripción pero sin personajes
     * @return Lista de DTOs de audiovisuales para administración
     * **/
    public List<AudiovisualAdminDTO> getAudiovisualsForAdmin() {
        return audiovisualRepository.findAllWithGenero().stream()
                .map(audiovisualMapper::audiovisualEntityToAudiovisualAdminDTO)
                .collect(Collectors.toList());
    }

    /** 
     * Obtiene un audiovisual por su ID
     * @param id ID del audiovisual a obtener
     * @return DTO de audiovisual con género
     * @throws ResourceNotFoundException si el audiovisual no existe
     * **/
    @Override
    public AudiovisualPrintDTO getAudiovisual(Long id) {
        return audiovisualRepository.findById(id)
                .map(audiovisualEntity -> {
                        AudiovisualPrintDTO audiovisualPrintDTO = audiovisualMapper.audiovisualEntityToAudiovisualPrintDTO(audiovisualEntity);
                        audiovisualPrintDTO.setPersonajes(audiovisualEntity.getPersonajes().stream()
                        .map(personajeMapper::personajeEntityToPersonajeListDTO)
                        .collect(Collectors.toList()));
                        return audiovisualPrintDTO;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Audiovisual no encontrado con ID: " + id));
    }

    /** 
     * Agrega un nuevo audiovisual
     * @param audiovisualDTO DTO de audiovisual a agregar
     * @throws FileProcessingException si ocurre un error al procesar la imagen
     * @throws ResourceNotFoundException si el género no existe
     * @throws ServiceException si ocurre un error al agregar el audiovisual
     * **/
    @Override
    public void addAudiovisual(AudiovisualDTO audiovisualDTO) {
        try {
            AudiovisualEntity audiovisualEntity = audiovisualMapper.audiovisualDTOtoAudiovisualEntity(audiovisualDTO);

            String fileName = audiovisualDTO.getImagen().getOriginalFilename();
            Path path = Paths.get(directory + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(audiovisualDTO.getImagen().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            audiovisualEntity.setImagen("images/audiovisual/" + fileName);
            audiovisualEntity.setGenero(generoRepository.findById(audiovisualDTO.getGeneroId())
                    .orElseThrow(() -> new ResourceNotFoundException("Genero no encontrado con el id: " + audiovisualDTO.getGeneroId())));

            audiovisualRepository.save(audiovisualEntity);
        } catch (IOException e) {
            throw new FileProcessingException("Error al procesar la imagen: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            throw e; // Re-throwing the ResourceNotFoundException
        } catch (Exception e) {
            throw new ServiceException("Error al agregar audiovisual: " + e.getMessage());
        }
    }

    /** 
     * Elimina un audiovisual
     * @param audiovisualId ID del audiovisual a eliminar
     * @throws ResourceNotFoundException si el audiovisual no existe
     * @throws ServiceException si ocurre un error al eliminar el audiovisual
     * **/
    @Override
    public void deleteAudiovisual(Long audiovisualId) {
        try {
            AudiovisualEntity audiovisual = audiovisualRepository.findById(audiovisualId)
                    .orElseThrow(() -> new ResourceNotFoundException("Audiovisual no encontrado con ID: " + audiovisualId));
            // Eliminar la imagen del sistema de archivos
            String imagePath = audiovisual.getImagen();
            if (imagePath != null) {
                // Convertir la ruta de la base de datos a la ruta del sistema de archivos
                String filePath = directory + imagePath.replace("images/audiovisual/", "");
                File imageFile = new File(filePath);
                if (imageFile.exists()) {
                    boolean deleted = imageFile.delete();
                    if (!deleted) {
                        log.warn("No se pudo eliminar la imagen: " + filePath);
                    }
                }
            }
            audiovisualRepository.deleteById(audiovisualId);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al eliminar el audiovisual con ID: " + audiovisualId, e);
            throw new ServiceException("Error al eliminar el audiovisual: " + e.getMessage());
        }
    }

    /** 
     * Actualiza un audiovisual
     * @param id ID del audiovisual a actualizar
     * @param audiovisualDTO DTO de audiovisual a actualizar
     * @throws ResourceNotFoundException si el audiovisual no existe
     * @throws ServiceException si ocurre un error al actualizar el audiovisual
     * **/
    @Override
    public void updateAudiovisual(Long id, AudiovisualDTO audiovisualDTO) {
        try {
            AudiovisualEntity currentAudiovisual = audiovisualRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Audiovisual no encontrado con ID: " + id));
            currentAudiovisual.setTitulo(audiovisualDTO.getTitulo());
            currentAudiovisual.setDescripcion(audiovisualDTO.getDescripcion());
            currentAudiovisual.setDirector(audiovisualDTO.getDirector());
            currentAudiovisual.setDuracion(audiovisualDTO.getDuracion());
            currentAudiovisual.setCalificacion(audiovisualDTO.getCalificacion());
            
            if (audiovisualDTO.getFechaCreacion() != null) {
                currentAudiovisual.setFechaCreacion(audiovisualDTO.getFechaCreacion());
            }
            
            // Buscar el género correcto usando el generoId del DTO
            GeneroEntity generoEntity = generoRepository.findById(audiovisualDTO.getGeneroId())
                    .orElseThrow(() -> new ResourceNotFoundException("Género no encontrado con ID: " + audiovisualDTO.getGeneroId()));
            currentAudiovisual.setGenero(generoEntity);

            if (audiovisualDTO.getImagen() != null && audiovisualDTO.getImagen().getOriginalFilename() != null) {
                // Eliminar la imagen anterior si existe
                String oldImagePath = currentAudiovisual.getImagen();
                if (oldImagePath != null) {
                    // Convertir la ruta de la base de datos a la ruta del sistema de archivos
                    String oldFilePath = directory + oldImagePath.replace("images/audiovisual/", "");
                    File oldImageFile = new File(oldFilePath);
                    if (oldImageFile.exists()) {
                        oldImageFile.delete(); // Borrar la imagen anterior
                    }
                }

                // Guardar la nueva imagen
                String fileName = audiovisualDTO.getImagen().getOriginalFilename();
                Path path = Paths.get(directory + fileName);
                Files.createDirectories(path.getParent());
                Files.copy(audiovisualDTO.getImagen().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                currentAudiovisual.setImagen("images/audiovisual/" + fileName);
            }

            audiovisualRepository.save(currentAudiovisual);
        } catch (ResourceNotFoundException e) {
            throw e; // Re-throwing the ResourceNotFoundException
        } catch (IOException e) {
            throw new FileProcessingException("Error al procesar la imagen: " + e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Error al modificar audiovisual: " + e.getMessage());
        }
    }

    /** 
     * Agrega un personaje a un audiovisual
     * @param id ID del audiovisual
     * @param personajeId ID del personaje a agregar
     * @throws ResourceNotFoundException si el audiovisual o el personaje no existen
     * @throws ServiceException si ocurre un error al agregar el personaje al audiovisual
     * **/
    @Override
    public void addCharacter(Long id, Long personajeId) {
        try {
            AudiovisualEntity audiovisual = audiovisualRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Audiovisual no encontrado con ID: " + id));

            PersonajeEntity personaje = personajeRepository.findById(personajeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Personaje no encontrado con ID: " + personajeId));

            audiovisual.addCharacter(personaje);
            personaje.addAudiovisual(audiovisual);
            personajeRepository.save(personaje);
        } catch (ResourceNotFoundException e) {
            throw e; // Re-throwing the ResourceNotFoundException
        } catch (Exception e) {
            throw new ServiceException("Error al agregar el personaje al audiovisual: " + e.getMessage());
        }
    }

    /** 
     * Elimina un personaje de un audiovisual
     * @param id ID del audiovisual
     * @param personajeId ID del personaje a eliminar
     * @throws ResourceNotFoundException si el audiovisual o el personaje no existen
     * @throws ServiceException si ocurre un error al eliminar el personaje del audiovisual
     * **/
    @Override
    public void removeCharacter(Long id, Long personajeId) {
        try {
            AudiovisualEntity audiovisual = audiovisualRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Audiovisual no encontrado con ID: " + id));

            PersonajeEntity personaje = personajeRepository.findById(personajeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Personaje no encontrado con ID: " + personajeId));

            audiovisual.removeCharacter(personaje);
            personaje.removeAudiovisual(audiovisual);
            personajeRepository.save(personaje);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw  new ServiceException("Error al eliminar el personaje del audiovisual: " + e.getMessage());
        }
    }

    /** 
     * Actualiza el género de un audiovisual
     * @param id ID del audiovisual
     * @param genreId ID del género a actualizar
     * @throws ResourceNotFoundException si el audiovisual o el género no existen
     * @throws ServiceException si ocurre un error al actualizar el género del audiovisual
     * **/
    @Override
    public void updateGenre(Long id, Long genreId) {
        try {
            AudiovisualEntity audiovisual = audiovisualRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Audiovisual no encontrado con ID: " + id));

            GeneroEntity genero = generoRepository.findById(genreId)
                    .orElseThrow(() -> new ResourceNotFoundException("Género no encontrado con ID: " + genreId));

            audiovisual.setGenero(genero);
            genero.addAudiovisual(audiovisual);

            generoRepository.save(genero);
        } catch (Exception e) {
            throw  new ServiceException("Error al agregar el genre del audiovisual: " + e.getMessage());
        }
    }
    
    /** 
     * Obtiene todos los audiovisuales ordenados por fecha de creación
     * @param order Orden de los audiovisuales (asc o desc)
     * @return Lista de DTOs de audiovisuales
     * **/
    /* @Override
    public List<AudiovisualPrintDTO> getAudiovisualsByOrder(String order) {
        List<AudiovisualEntity> audiovisuals;
        if (order.equals("asc")) {
            audiovisuals = audiovisualRepository.findAllByOrderByFechaCreacionAsc();
        } else {
            audiovisuals = audiovisualRepository.findAllByOrderByFechaCreacionDesc();
        }
        return audiovisuals.stream()
                .map(audiovisualEntityToAudiovisualPrintDTO::convert)
                .collect(Collectors.toList());
    } */

    /** 
     * Obtiene todos los audiovisuales por género
     * @param genreId ID del género
     * @return Lista de DTOs de audiovisuales
     * **/
    /*@Override
    public List<AudiovisualPrintDTO> getAudiovisualsByGenre(Long genreId) {
        List<AudiovisualEntity> audiovisuals = audiovisualRepository.findByGenero(genreId);
        return audiovisuals.stream()
                .map(audiovisualEntityToAudiovisualPrintDTO::convert)
                .collect(Collectors.toList());
    } */

    /** 
     * Obtiene todos los audiovisuales por título
     * @param audiovisualTitle Título del audiovisual
     * @return Lista de DTOs de audiovisuales
     * **/
    /*@Override
    public List<AudiovisualPrintDTO> getAudiovisualsByTitle(String audiovisualTitle) {
        List<AudiovisualEntity> audiovisuals = audiovisualRepository.findByTituloLike(audiovisualTitle);
        return audiovisuals.stream()
                .map(audiovisualEntityToAudiovisualPrintDTO::convert)
                .collect(Collectors.toList());
    } */

    public List<AudiovisualListDTO> getTopAudiovisuals(int limit) {
        return audiovisualRepository.findAllWithGenero().stream()
            .map(audiovisualMapper::audiovisualEntityToAudiovisualListDTO)
            .limit(limit)
            .collect(Collectors.toList());
    }
}