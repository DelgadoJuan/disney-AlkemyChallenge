package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.Converter.PersonajeDTOToPersonajeEntity;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeListDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.PersonajeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonajeServiceImplTest {

    @Mock
    private PersonajeRepository personajeRepository;

    @Mock
    private PersonajeDTOToPersonajeEntity personajeDTOToPersonajeEntity;

    @InjectMocks
    private PersonajeServiceImpl personajeService;

    private PersonajeDTO personajeDTO;
    private PersonajeEntity personajeEntity;
    private PersonajeEntity personajeEntity2;
    private AudiovisualEntity audiovisualEntity;
    private GeneroEntity generoEntity;

    @BeforeEach
    void setUp() {
        // DTO
        personajeDTO = new PersonajeDTO();
        personajeDTO.setNombre("Pato Donald");
        personajeDTO.setEdad(85);
        personajeDTO.setPeso(20);
        personajeDTO.setHistoria("Historia de Pato Donald");
        MockMultipartFile patoImage = new MockMultipartFile(
                "imagen", "patodonald.jpg", "image/jpeg", "imagen de prueba".getBytes()
        );
        personajeDTO.setImagen(patoImage);

        // Entity
        personajeEntity = new PersonajeEntity();
        personajeEntity.setNombre("Pato Donald");
        personajeEntity.setEdad(85);
        personajeEntity.setPeso(20);
        personajeEntity.setHistoria("Historia de Pato Donald");
        personajeEntity.setImagen("src/main/resources/static/images/personaje/patodonald.jpg");

        personajeEntity2 = new PersonajeEntity();
        personajeEntity2.setId(2L);
        personajeEntity2.setNombre("Pato enojado");
        personajeEntity2.setEdad(32);
        personajeEntity2.setPeso(15);
        personajeEntity2.setHistoria("Historia de Pato enojado");
        personajeEntity2.setImagen("src/main/resources/static/images/personaje/patoenojado.jpg");

        // Genero
        generoEntity = new GeneroEntity();
        generoEntity.setId(1L);
        generoEntity.setNombre("Infantil");
        generoEntity.setImagen("src/main/resources/static/images/genero/infantil.jpg");

        // Audiovisual
        audiovisualEntity = new AudiovisualEntity();
        audiovisualEntity.setId(1L);
        audiovisualEntity.setTitulo("Mickey Mouse Clubhouse");
        audiovisualEntity.setImagen("src/main/resources/static/images/audiovisual/mickeymouseclubhouse.jpg");
        audiovisualEntity.setFechaCreacion(LocalDate.of(2006, 5, 5));
        audiovisualEntity.setCalificacion(3);
        audiovisualEntity.setGenero(generoEntity);
    }

    @Test
    void charactersList() {
        when(personajeRepository.findAll()).thenReturn(List.of(personajeEntity, personajeEntity2));

        List<PersonajeListDTO> result = personajeService.charactersList();

        assertNotNull(result);
        assertEquals(2, result.size());
        // Pato Donald
        assertEquals("Pato Donald", result.get(0).getNombre());
        assertEquals("src/main/resources/static/images/personaje/patodonald.jpg", result.get(0).getImagen());
        // Pato enojado
        assertEquals("Pato enojado", result.get(1).getNombre());
        assertEquals("src/main/resources/static/images/personaje/patoenojado.jpg", result.get(1).getImagen());
        verify(personajeRepository, times(1)).findAll();
    }

    @Test
    void getCharacter() {
        when(personajeRepository.findById(1L)).thenReturn(java.util.Optional.of(personajeEntity));

        Optional<PersonajeEntity> result = personajeService.getCharacter(1L);
        assertTrue(result.isPresent());
        assertEquals("Pato Donald", result.get().getNombre());
        assertEquals("src/main/resources/static/images/personaje/patodonald.jpg", result.get().getImagen());
        assertEquals(85, result.get().getEdad());
        assertEquals(20, result.get().getPeso());
        assertEquals("Historia de Pato Donald", result.get().getHistoria());
        verify(personajeRepository, times(1)).findById(1L);
    }

    @Test
    void getCharactersByName() {
        when(personajeRepository.findByNombre("Pato")).thenReturn(List.of(personajeEntity, personajeEntity2));

        List<PersonajeEntity> result = personajeService.getCharactersByName("Pato");
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pato Donald", result.get(0).getNombre());
        assertEquals("Pato enojado", result.get(1).getNombre());
        verify(personajeRepository, times(1)).findByNombre("Pato");
    }

    @Test
    void getCharactersByAge() {
        when(personajeRepository.findByEdad(85)).thenReturn(List.of(personajeEntity));

        List<PersonajeEntity> result = personajeService.getCharactersByAge(85);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pato Donald", result.get(0).getNombre());
        verify(personajeRepository, times(1)).findByEdad(85);
    }

    @Test
    void getCharactersByWeight() {
        when(personajeRepository.findByPeso(20)).thenReturn(List.of(personajeEntity));

        List<PersonajeEntity> result = personajeService.getCharactersByWeight(20);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pato Donald", result.get(0).getNombre());
        verify(personajeRepository, times(1)).findByPeso(20);
    }

    @Test
    void getCharactersByAudiovisual() {
        when(personajeRepository.findByAudiovisuales(1L)).thenReturn(List.of(personajeEntity,
                personajeEntity2));

        List<PersonajeEntity> result = personajeService.getCharactersByAudiovisual(1L);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pato Donald", result.get(0).getNombre());
        assertEquals("Pato enojado", result.get(1).getNombre());
        verify(personajeRepository, times(1)).findByAudiovisuales(1L);
    }

    @Test
    void addCharacter() {
        // Configura el comportamiento del convertidor
        when(personajeDTOToPersonajeEntity.convert(personajeDTO)).thenReturn(personajeEntity);

        // Simulación del comportamiento del repositorio
        when(personajeRepository.save(any(PersonajeEntity.class))).thenReturn(personajeEntity);

        boolean result = personajeService.addCharacter(personajeDTO);

        assertTrue(result);
        verify(personajeRepository, times(1)).save(any(PersonajeEntity.class));
    }

    @Test
    void removeCharacter() {
        doNothing().when(personajeRepository).deleteById(1L);

        boolean result = personajeService.removeCharacter(1L);
        assertTrue(result);
        verify(personajeRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateCharacter() {
        when(personajeRepository.findById(1L)).thenReturn(Optional.of(personajeEntity));
        when(personajeRepository.save(any(PersonajeEntity.class))).thenReturn(personajeEntity);

        boolean result = personajeService.updateCharacter(1L, personajeDTO);
        assertTrue(result);
        verify(personajeRepository, times(1)).findById(1L);
        verify(personajeRepository, times(1)).save(any(PersonajeEntity.class));
    }
}