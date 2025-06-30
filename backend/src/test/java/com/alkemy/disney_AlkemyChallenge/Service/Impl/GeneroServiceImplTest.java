package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.AudiovisualRepository;
import com.alkemy.disney_AlkemyChallenge.Repository.GeneroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeneroServiceImplTest {

    @Mock
    private GeneroRepository generoRepository;

    @Mock
    private AudiovisualRepository audiovisualRepository;

    @InjectMocks
    private GeneroServiceImpl generoService;

    private final String directory = "src/main/resources/static/images/genero/";

    @Test
    void addGenero() {
        // Crear un MockMultipartFile para simular la imagen
        MockMultipartFile imageFile = new MockMultipartFile(
                "imagen", "drama.jpg", "image/jpeg", "imagen de prueba".getBytes()
        );

        // Crear DTO con el archivo
        GeneroDTO generoDTO = new GeneroDTO();
        generoDTO.setNombre("Drama");
        generoDTO.setImagen(imageFile);

        GeneroEntity generoEntity = new GeneroEntity();
        generoEntity.setNombre("Drama");
        generoEntity.setImagen("src/main/resources/static/images/genero/drama.jpg");

        when(generoRepository.save(any(GeneroEntity.class))).thenReturn(generoEntity);

        boolean result = generoService.addGenero(generoDTO);

        assertTrue(result);
        verify(generoRepository, times(1)).save(any(GeneroEntity.class));
    }

    @Test
    void getGeneros() {
        generoService.getGeneros();

        verify(generoRepository, times(1)).findAll();
    }

    @Test
    void updateGenero() {
        // Crear un MockMultipartFile para simular la imagen
        MockMultipartFile newImageFile = new MockMultipartFile(
                "nueva_imagen_url", "accion.jpg", "image/jpeg", "nueva imagen".getBytes()
        );

        // Crear DTO con el archivo
        GeneroDTO generoDTO = new GeneroDTO();
        generoDTO.setNombre("Acción");
        generoDTO.setImagen(newImageFile);

        GeneroEntity currentGenero = new GeneroEntity();
        currentGenero.setNombre("Drama");
        currentGenero.setImagen("imagen_url");

        when(generoRepository.findById(1L)).thenReturn(Optional.of(currentGenero));
        when(generoRepository.save(any(GeneroEntity.class))).thenReturn(currentGenero);

        boolean result = generoService.updateGenero(1L, generoDTO);

        assertTrue(result);
        assertEquals("Acción", currentGenero.getNombre());
        assertEquals(directory + "accion.jpg", currentGenero.getImagen());
        verify(generoRepository, times(1)).save(currentGenero);
    }

    @Test
    void deleteGenero() {
        doNothing().when(generoRepository).deleteById(1L);

        boolean result = generoService.deleteGenero(1L);

        assertTrue(result);
        verify(generoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAudiovisualFromGenre() {
        GeneroEntity genero = new GeneroEntity();
        AudiovisualEntity audiovisual = new AudiovisualEntity();

        when(generoRepository.findById(1L)).thenReturn(Optional.of(genero));
        when(audiovisualRepository.findById(2L)).thenReturn(Optional.of(audiovisual));

        // Ejecuta el metodo bajo prueba
        boolean result = generoService.deleteAudiovisualFromGenre(1L, 2L);

        // Imprime el resultado para depuración
        System.out.println("Result: " + result);

        // Verifica que el resultado es true, ya que la eliminación debería haber sido exitosa
        assertTrue(result);

        // Verifica que los métodos del repositorio hayan sido llamados con los argumentos correctos
        verify(generoRepository).save(genero);
    }
}