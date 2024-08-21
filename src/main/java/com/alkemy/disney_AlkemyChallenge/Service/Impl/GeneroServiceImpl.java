package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.AudiovisualRepository;
import com.alkemy.disney_AlkemyChallenge.Repository.GeneroRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IGeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class GeneroServiceImpl implements IGeneroService {
    private final AudiovisualRepository AudiovisualRepository;
    private final GeneroRepository generoRepository;
    private final String directory = "src/main/resources/static/images/genero/";

    @Autowired
    public GeneroServiceImpl(com.alkemy.disney_AlkemyChallenge.Repository.AudiovisualRepository audiovisualRepository, GeneroRepository generoRepository) {
        AudiovisualRepository = audiovisualRepository;
        this.generoRepository = generoRepository;
    }

    @Override
    public boolean addGenero(GeneroDTO generoDTO) {
        try {
            String fileName = generoDTO.getImagen().getOriginalFilename();

            Path path = Paths.get(directory + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(generoDTO.getImagen().getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

            GeneroEntity generoEntity = new GeneroEntity(generoDTO.getNombre(), directory + fileName);
            generoRepository.save(generoEntity);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<GeneroEntity> getGeneros() {
        return generoRepository.findAll();
    }

    @Override
    public boolean updateGenero(Long id, GeneroDTO generoDTO) {
        try {
            GeneroEntity currentGenero = generoRepository.findById(id).orElseThrow();

            // Eliminar la imagen anterior si existe
            String oldImagePath = currentGenero.getImagen();
            if (oldImagePath != null) {
                File oldImageFile = new File(oldImagePath);
                if (oldImageFile.exists()) {
                    oldImageFile.delete(); // Borrar la imagen anterior
                }
            }

            // Guardar la nueva imagen
            String fileName = generoDTO.getImagen().getOriginalFilename();
            Path path = Paths.get(directory + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(generoDTO.getImagen().getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

            // Actualizar la entidad con la nueva información
            currentGenero.setNombre(generoDTO.getNombre());
            currentGenero.setImagen(directory + fileName);
            generoRepository.save(currentGenero);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteGenero(Long id) {
        try {
            generoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteAudiovisualFromGenre(Long id, Long audiovisualId) {
        try {
            GeneroEntity generoEntity = generoRepository.findById(id).orElseThrow();
            AudiovisualEntity audiovisualEntity = AudiovisualRepository.findById(audiovisualId).orElseThrow();

            generoEntity.removeAudiovisual(audiovisualEntity);
            audiovisualEntity.setGenero(null);
            AudiovisualRepository.save(audiovisualEntity);
            generoRepository.save(generoEntity);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}