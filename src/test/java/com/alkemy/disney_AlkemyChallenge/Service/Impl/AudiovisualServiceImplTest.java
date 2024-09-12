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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AudiovisualServiceImplTest {
    @Mock
    private AudiovisualEntityToAudiovisualDTO audiovisualEntityToAudiovisualDTO;
    @Mock
    private AudiovisualDTOToAudiovisualEntity audiovisualDTOToAudiovisualEntity;
    @Mock
    private AudiovisualRepository audiovisualRepository;
    @Mock
    private GeneroRepository generoRepository;
    @Mock
    private PersonajeRepository personajeRepository;
    @InjectMocks
    private AudiovisualServiceImpl audiovisualService;

    private AudiovisualDTO audiovisualDTO;
    private AudiovisualEntity audiovisualEntity;
    private AudiovisualEntity audiovisualEntity2;
    private GeneroEntity generoEntity;
    private AudiovisualPrintDTO audiovisualPrintDTO;
    private AudiovisualPrintDTO audiovisualPrintDTO2;
    private PersonajeEntity personajeEntity;

    @BeforeEach
    void setUp() {
        audiovisualDTO = new AudiovisualDTO();
        audiovisualDTO.setTitulo("El Rey León");
        audiovisualDTO.setFechaCreacion(LocalDate.of(1994, 6, 24));
        audiovisualDTO.setCalificacion(5);
        MockMultipartFile imageFile = new MockMultipartFile(
                "imagen", "el-rey-leon.jpg", "image/jpeg", "imagen de prueba".getBytes()
        );
        audiovisualDTO.setImagen(imageFile);

        generoEntity = new GeneroEntity();
        generoEntity.setNombre("Animación");

        audiovisualEntity = new AudiovisualEntity();
        audiovisualEntity.setTitulo("El Rey León");
        audiovisualEntity.setFechaCreacion(LocalDate.of(1994, 6, 24));
        audiovisualEntity.setCalificacion(5);
        audiovisualEntity.setImagen("src/main/resources/static/images/audiovisual/el-rey-leon.jpg");
        audiovisualEntity.setGenero(generoEntity);

        audiovisualEntity2 = new AudiovisualEntity();
        audiovisualEntity2.setTitulo("Frozen");
        audiovisualEntity2.setFechaCreacion(LocalDate.of(2013, 11, 27));
        audiovisualEntity2.setCalificacion(4);
        audiovisualEntity2.setImagen("src/main/resources/static/images/audiovisual/frozen.jpg");
        audiovisualEntity2.setGenero(generoEntity);

        audiovisualPrintDTO = new AudiovisualPrintDTO();
        audiovisualPrintDTO.setTitulo("El Rey León");
        audiovisualPrintDTO.setFechaCreacion(LocalDate.of(1994, 6, 24));
        audiovisualPrintDTO.setCalificacion(5);
        audiovisualPrintDTO.setImagen("src/main/resources/static/images/audiovisual/el-rey-leon.jpg");
        audiovisualPrintDTO.setNombreGenero("Animación");

        audiovisualPrintDTO2 = new AudiovisualPrintDTO();
        audiovisualPrintDTO2.setTitulo("Frozen");
        audiovisualPrintDTO2.setFechaCreacion(LocalDate.of(2013, 11, 27));
        audiovisualPrintDTO2.setCalificacion(4);
        audiovisualPrintDTO2.setImagen("src/main/resources/static/images/audiovisual/frozen.jpg");
        audiovisualPrintDTO2.setNombreGenero("Animación");

        personajeEntity = new PersonajeEntity();
        personajeEntity.setNombre("Pato Donald");
        personajeEntity.setEdad(85);
        personajeEntity.setPeso(20);
        personajeEntity.setHistoria("Historia de Pato Donald");
        personajeEntity.setImagen("src/main/resources/static/images/personaje/patodonald.jpg");
    }

    @Test
    void getAudiovisuals() {
        when(audiovisualRepository.findAll()).thenReturn(List.of(audiovisualEntity, audiovisualEntity2));

        List<AudiovisualListDTO> result = audiovisualService.getAudiovisuals();
        assertEquals(2, result.size());
        assertEquals("El Rey León", result.get(0).getTitulo());
        assertEquals("Frozen", result.get(1).getTitulo());
        verify(audiovisualRepository, times(1)).findAll();
    }

    @Test
    void getAudiovisualsByOrder() {
        // Configuración para el orden ascendente
        when(audiovisualEntityToAudiovisualDTO.convert(audiovisualEntity)).thenReturn(audiovisualPrintDTO);
        when(audiovisualEntityToAudiovisualDTO.convert(audiovisualEntity2)).thenReturn(audiovisualPrintDTO2);
        when(audiovisualRepository.findAllByOrderByFechaCreacionAsc()).thenReturn(List.of(audiovisualEntity, audiovisualEntity2));

        // Test para el orden ascendente
        List<AudiovisualPrintDTO> resultAsc = audiovisualService.getAudiovisualsByOrder("asc");
        assertEquals(2, resultAsc.size());
        assertEquals("El Rey León", resultAsc.get(0).getTitulo());
        assertEquals("Frozen", resultAsc.get(1).getTitulo());
        verify(audiovisualRepository, times(1)).findAllByOrderByFechaCreacionAsc();

        // Configuración para el orden descendente
        when(audiovisualRepository.findAllByOrderByFechaCreacionDesc()).thenReturn(List.of(audiovisualEntity2, audiovisualEntity));

        // Test para el orden descendente
        List<AudiovisualPrintDTO> resultDesc = audiovisualService.getAudiovisualsByOrder("desc");
        assertEquals(2, resultDesc.size());
        assertEquals("Frozen", resultDesc.get(0).getTitulo());
        assertEquals("El Rey León", resultDesc.get(1).getTitulo());
        verify(audiovisualRepository, times(1)).findAllByOrderByFechaCreacionDesc();
    }

    @Test
    void getAudiovisualsByGenre() {
        when(audiovisualEntityToAudiovisualDTO.convert(audiovisualEntity)).thenReturn(audiovisualPrintDTO);
        when(audiovisualEntityToAudiovisualDTO.convert(audiovisualEntity2)).thenReturn(audiovisualPrintDTO2);
        when(audiovisualRepository.findByGenero(1L)).thenReturn(List.of(audiovisualEntity, audiovisualEntity2));

        List<AudiovisualPrintDTO> result = audiovisualService.getAudiovisualsByGenre(1L);
        assertEquals(2, result.size());
        assertEquals("El Rey León", result.get(0).getTitulo());
        assertEquals("Frozen", result.get(1).getTitulo());
        verify(audiovisualRepository, times(1)).findByGenero(1L);
    }

    @Test
    void getAudiovisualsByTitle() {
        when(audiovisualEntityToAudiovisualDTO.convert(audiovisualEntity)).thenReturn(audiovisualPrintDTO);
        when(audiovisualRepository.findByTituloLike("El Rey León")).thenReturn(List.of(audiovisualEntity));

        List<AudiovisualPrintDTO> result = audiovisualService.getAudiovisualsByTitle("El Rey León");
        assertEquals(1, result.size());
        assertEquals("El Rey León", result.get(0).getTitulo());
        verify(audiovisualRepository, times(1)).findByTituloLike("El Rey León");
    }

    @Test
    void getAudiovisual() {
        when(audiovisualRepository.findById(1L)).thenReturn(java.util.Optional.of(audiovisualEntity));

        AudiovisualEntity result = audiovisualService.getAudiovisual(1L);
        assertEquals("El Rey León", result.getTitulo());
        verify(audiovisualRepository, times(1)).findById(1L);
    }

    @Test
    void addAudiovisual() {
        when(audiovisualDTOToAudiovisualEntity.convert(audiovisualDTO)).thenReturn(audiovisualEntity);
        when(audiovisualRepository.save(any(AudiovisualEntity.class))).thenReturn(audiovisualEntity);

        boolean result = audiovisualService.addAudiovisual(audiovisualDTO);
        assertTrue(result);
        verify(audiovisualRepository, times(1)).save(any(AudiovisualEntity.class));
    }

    @Test
    void deleteAudiovisual() {
        doNothing().when(audiovisualRepository).deleteById(1L);

        boolean result = audiovisualService.deleteAudiovisual(1L);
        assertTrue(result);
        verify(audiovisualRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateAudiovisual() {
        when(audiovisualRepository.findById(1L)).thenReturn(java.util.Optional.of(audiovisualEntity));
        when(audiovisualRepository.save(any(AudiovisualEntity.class))).thenReturn(audiovisualEntity);
        when(generoRepository.findById(1L)).thenReturn(java.util.Optional.of(generoEntity));
        when(generoRepository.save(any(GeneroEntity.class))).thenReturn(generoEntity);

        boolean result = audiovisualService.updateAudiovisual(1L, audiovisualDTO);
        assertTrue(result);
        verify(audiovisualRepository, times(1)).findById(1L);
        verify(audiovisualRepository, times(1)).save(any(AudiovisualEntity.class));
    }

    @Test
    void addCharacter() {
        when(audiovisualRepository.findById(1L)).thenReturn(java.util.Optional.of(audiovisualEntity));
        when(personajeRepository.findById(1L)).thenReturn(java.util.Optional.of(personajeEntity));
        when(personajeRepository.save(any(PersonajeEntity.class))).thenReturn(personajeEntity);

        boolean result = audiovisualService.addCharacter(1L, 1L);
        assertTrue(result);
        verify(audiovisualRepository, times(1)).findById(1L);
        verify(personajeRepository, times(1)).findById(1L);
        verify(personajeRepository, times(1)).save(any(PersonajeEntity.class));
    }

    @Test
    void removeCharacter() {
        when(audiovisualRepository.findById(1L)).thenReturn(java.util.Optional.of(audiovisualEntity));
        when(personajeRepository.findById(1L)).thenReturn(java.util.Optional.of(personajeEntity));
        when(personajeRepository.save(any(PersonajeEntity.class))).thenReturn(personajeEntity);

        boolean result = audiovisualService.removeCharacter(1L, 1L);
        assertTrue(result);
        verify(audiovisualRepository, times(1)).findById(1L);
        verify(personajeRepository, times(1)).findById(1L);
        verify(personajeRepository, times(1)).save(any(PersonajeEntity.class));
    }

    @Test
    void updateGenre() {
        when(audiovisualRepository.findById(1L)).thenReturn(java.util.Optional.of(audiovisualEntity));
        when(generoRepository.findById(1L)).thenReturn(java.util.Optional.of(generoEntity));
        when(generoRepository.save(any(GeneroEntity.class))).thenReturn(generoEntity);

        boolean result = audiovisualService.updateGenre(1L, 1L);
        assertTrue(result);
        verify(audiovisualRepository, times(1)).findById(1L);
        verify(generoRepository, times(1)).findById(1L);
        verify(generoRepository, times(1)).save(any(GeneroEntity.class));
    }
}