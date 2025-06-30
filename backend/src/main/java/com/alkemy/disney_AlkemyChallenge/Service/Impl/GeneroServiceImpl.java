package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroResponseDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import com.alkemy.disney_AlkemyChallenge.Mapper.GeneroMapper;
import com.alkemy.disney_AlkemyChallenge.Repository.AudiovisualRepository;
import com.alkemy.disney_AlkemyChallenge.Repository.GeneroRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IGeneroService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneroServiceImpl implements IGeneroService {
    private final AudiovisualRepository AudiovisualRepository;
    private final GeneroRepository generoRepository;
    private final String directory = "src/main/resources/static/images/genero/";
    private final GeneroMapper generoMapper;

    /** 
     * Agrega un nuevo género
     * @param generoDTO DTO de género a agregar
     * @return true si el género se agregó correctamente, false en caso contrario
     * **/
    @Override
    public boolean addGenero(GeneroDTO generoDTO) {
        try {
            String fileName = generoDTO.getImagen().getOriginalFilename();

            Path path = Paths.get(directory + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(generoDTO.getImagen().getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

            GeneroEntity generoEntity = generoMapper.generoDTOtoGeneroEntity(generoDTO);
            generoEntity.setImagen("images/genero/" + fileName);
            generoRepository.save(generoEntity);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /** 
     * Obtiene todos los géneros
     * @return Lista de DTOs de géneros
     * **/
    @Override
    public List<GeneroResponseDTO> getGeneros() {
        return generoRepository.findAll()
                .stream()
                .map(generoMapper::generoEntityToGeneroResponseDTO)
                .collect(Collectors.toList());
    }

    /** 
     * Actualiza un género
     * @param id ID del género a actualizar
     * @param generoDTO DTO de género a actualizar
     * @return true si el género se actualizó correctamente, false en caso contrario
     * **/
    @Override
    public boolean updateGenero(Long id, GeneroDTO generoDTO) {
        try {
            GeneroEntity currentGenero = generoRepository.findById(id).orElseThrow();

            if (generoDTO.getImagen() != null && generoDTO.getImagen().getOriginalFilename() != null) {
                // Eliminar la imagen anterior si existe
                String oldImagePath = currentGenero.getImagen();
                if (oldImagePath != null) {
                    // Convertir la ruta de la base de datos a la ruta del sistema de archivos
                    String oldFilePath = directory + oldImagePath.replace("images/genero/", "");
                    File oldImageFile = new File(oldFilePath);
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
                currentGenero.setImagen("images/genero/" + fileName);
            }

            generoRepository.save(currentGenero);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** 
     * Elimina un género
     * @param id ID del género a eliminar
     * @return true si el género se eliminó correctamente, false en caso contrario
     * **/
    @Override
    public boolean deleteGenero(Long id) {
        try {
            GeneroEntity generoEntity = generoRepository.findById(id).orElseThrow();
            // Eliminar la imagen del género
            String imagePath = generoEntity.getImagen();
            if (imagePath != null) {
                // Convertir la ruta de la base de datos a la ruta del sistema de archivos
                String filePath = directory + imagePath.replace("images/genero/", "");
                File imageFile = new File(filePath);
                if (imageFile.exists()) {
                    imageFile.delete(); // Borrar la imagen del género
                }
            }
            generoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** 
     * Elimina un audiovisual de un género
     * @param id ID del género
     * @param audiovisualId ID del audiovisual a eliminar
     * @return true si el audiovisual se eliminó correctamente, false en caso contrario
     * **/
    @Override
    @Transactional
    public boolean deleteAudiovisualFromGenre(Long id, Long audiovisualId) {
        try {

            GeneroEntity generoEntity = generoRepository.findById(id).orElseThrow();
            AudiovisualEntity audiovisualEntity = AudiovisualRepository.findById(audiovisualId).orElseThrow();

            generoEntity.removeAudiovisual(audiovisualEntity);
            generoRepository.save(generoEntity);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}