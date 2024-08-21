package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.Converter.AudiovisualDTOToAudiovisualEntity;
import com.alkemy.disney_AlkemyChallenge.Converter.AudiovisualEntityToAudiovisualDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualListDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualPrintDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.AudiovisualRepository;
import com.alkemy.disney_AlkemyChallenge.Repository.GeneroRepository;
import com.alkemy.disney_AlkemyChallenge.Repository.PersonajeRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IAudiovisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AudiovisualServiceImpl implements IAudiovisualService {
    private final AudiovisualEntityToAudiovisualDTO audiovisualEntityToAudiovisualDTO;
    private final AudiovisualDTOToAudiovisualEntity audiovisualDTOToAudiovisualEntity;
    private final GeneroRepository generoRepository;
    private final PersonajeRepository personajeRepository;
    private final AudiovisualRepository audiovisualRepository;
    private final String directory = "src/main/resources/static/images/audiovisual/";

    @Autowired
    public AudiovisualServiceImpl(AudiovisualEntityToAudiovisualDTO audiovisualEntityToAudiovisualDTO, AudiovisualDTOToAudiovisualEntity audiovisualDTOToAudiovisualEntity, GeneroRepository generoRepository, PersonajeRepository personajeRepository,
                                  AudiovisualRepository audiovisualRepository) {
        this.audiovisualEntityToAudiovisualDTO = audiovisualEntityToAudiovisualDTO;
        this.audiovisualDTOToAudiovisualEntity = audiovisualDTOToAudiovisualEntity;
        this.generoRepository = generoRepository;
        this.personajeRepository = personajeRepository;
        this.audiovisualRepository = audiovisualRepository;
    }

    @Override
    public List<AudiovisualListDTO> getAudiovisuals() {
        return audiovisualRepository.findAll().stream()
                .map(audiovisual -> new AudiovisualListDTO(audiovisual.getImagen(), audiovisual.getTitulo(),
                        audiovisual.getFechaCreacion()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AudiovisualPrintDTO> getAudiovisualsByOrder(String order) {
        List<AudiovisualEntity> audiovisuals;
        if (order.equals("asc")) {
            audiovisuals = audiovisualRepository.findAllByOrderByFechaCreacionAsc();
        } else {
            audiovisuals = audiovisualRepository.findAllByOrderByFechaCreacionDesc();
        }
        return audiovisuals.stream()
                .map(audiovisualEntityToAudiovisualDTO::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<AudiovisualPrintDTO> getAudiovisualsByGenre(Long genreId) {
        List<AudiovisualEntity> audiovisuals = audiovisualRepository.findByGenero(genreId);
        return audiovisuals.stream()
                .map(audiovisualEntityToAudiovisualDTO::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<AudiovisualPrintDTO> getAudiovisualsByTitle(String audiovisualTitle) {
        List<AudiovisualEntity> audiovisuals = audiovisualRepository.findByTituloLike(audiovisualTitle);
        return audiovisuals.stream()
                .map(audiovisualEntityToAudiovisualDTO::convert)
                .collect(Collectors.toList());
    }

    @Override
    public AudiovisualEntity getAudiovisual(Long id) {
        return audiovisualRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un audiovisual con el ID: " + id));
    }

    @Override
    public boolean addAudiovisual(AudiovisualDTO audiovisualDTO) {
        try {
            AudiovisualEntity audiovisualEntity = audiovisualDTOToAudiovisualEntity.convert(audiovisualDTO);

            String fileName = audiovisualDTO.getImagen().getOriginalFilename();
            Path path = Paths.get(directory + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(audiovisualDTO.getImagen().getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

            audiovisualEntity.setImagen(directory + fileName);

            audiovisualRepository.save(audiovisualEntity);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAudiovisual(Long audiovisualId) {
        try {
            audiovisualRepository.deleteById(audiovisualId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateAudiovisual(Long id, AudiovisualDTO audiovisualDTO) {
        try {
            AudiovisualEntity currentAudiovisual = audiovisualRepository.findById(id).orElseThrow();
            currentAudiovisual.setTitulo(audiovisualDTO.getTitulo());
            GeneroEntity generoEntity = generoRepository.findById(id).orElseThrow();
            generoEntity.addAudiovisual(currentAudiovisual);
            currentAudiovisual.setGenero(generoEntity);
            currentAudiovisual.setCalificacion(audiovisualDTO.getCalificacion());

            // Eliminar la imagen anterior si existe
            String oldImagePath = currentAudiovisual.getImagen();
            if (oldImagePath != null) {
                File oldImageFile = new File(oldImagePath);
                if (oldImageFile.exists()) {
                    oldImageFile.delete(); // Borrar la imagen anterior
                }
            }

            // Guardar la nueva imagen
            String fileName = audiovisualDTO.getImagen().getOriginalFilename();
            Path path = Paths.get(directory + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(audiovisualDTO.getImagen().getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            currentAudiovisual.setImagen(directory + fileName);

            generoRepository.save(generoEntity);
            audiovisualRepository.save(currentAudiovisual);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addCharacter(Long id, Long personajeId) {
        try {
            AudiovisualEntity audiovisual = audiovisualRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "No se encontró un audiovisual con el ID: " + id));

            PersonajeEntity personaje = personajeRepository.findById(personajeId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "No se encontró un personaje con el ID: " + personajeId));

            audiovisual.addCharacter(personaje);
            personaje.addAudiovisual(audiovisual);
            personajeRepository.save(personaje);
            audiovisualRepository.save(audiovisual);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean removeCharacter(Long id, Long personajeId) {
        try {
            AudiovisualEntity audiovisual = audiovisualRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "No se encontró un audiovisual con el ID: " + id));

            PersonajeEntity personaje = personajeRepository.findById(personajeId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "No se encontró un personaje con el ID: " + personajeId));

            audiovisual.removeCharacter(personaje);
            personaje.removeAudiovisual(audiovisual);
            personajeRepository.save(personaje);
            audiovisualRepository.save(audiovisual);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
