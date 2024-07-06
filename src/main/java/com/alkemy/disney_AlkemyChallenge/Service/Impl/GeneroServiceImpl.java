package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.AudiovisualRepository;
import com.alkemy.disney_AlkemyChallenge.Repository.GeneroRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IGeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneroServiceImpl implements IGeneroService {
    private final AudiovisualRepository AudiovisualRepository;
    private final GeneroRepository generoRepository;

    @Autowired
    public GeneroServiceImpl(com.alkemy.disney_AlkemyChallenge.Repository.AudiovisualRepository audiovisualRepository, GeneroRepository generoRepository) {
        AudiovisualRepository = audiovisualRepository;
        this.generoRepository = generoRepository;
    }

    @Override
    public boolean addGenero(GeneroEntity generoEntity) {
        try {
            generoRepository.save(generoEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<GeneroEntity> getGeneros() {
        return generoRepository.findAll();
    }

    @Override
    public boolean updateGenero(Long id, GeneroEntity generoEntity) {
        try {
            GeneroEntity currentGenero = generoRepository.findById(id).orElseThrow();
            currentGenero.setNombre(generoEntity.getNombre());
            currentGenero.setImagen(generoEntity.getImagen());
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
