package com.alkemy.disney_AlkemyChallenge.Controller;

import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualListDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualPrintDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.GeneroRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IAudiovisualService;
import com.alkemy.disney_AlkemyChallenge.Service.IJWTUtilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AudiovisualController.class)
class AudiovisualControllerTest {
    @MockBean
    private IJWTUtilityService jwtUtilityService;
    @MockBean
    private IAudiovisualService audiovisualService;
    @MockBean
    private GeneroRepository generoRepository;

    @Autowired
    private MockMvc mockMvc;

    private AudiovisualDTO audiovisualDTO;
    private AudiovisualEntity audiovisualEntity;
    private AudiovisualEntity audiovisualEntity2;
    private AudiovisualListDTO audiovisualListDTO;
    private AudiovisualListDTO audiovisualListDTO2;
    private AudiovisualPrintDTO audiovisualPrintDTO;
    private AudiovisualPrintDTO audiovisualPrintDTO2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AudiovisualController(audiovisualService))
                .build();

        audiovisualDTO = new AudiovisualDTO();
        audiovisualDTO.setTitulo("La Bella y La Bestia");
        audiovisualDTO.setFechaCreacion(LocalDate.of(1991, 11, 22));
        audiovisualDTO.setCalificacion(3);
        MockMultipartFile bellaBestia = new MockMultipartFile(
                "imagen", "BellayBestia.jpg", "image/jpeg", "imagen de prueba".getBytes()
        );
        audiovisualDTO.setImagen(bellaBestia);
        audiovisualDTO.setGeneroId(1L);

        audiovisualEntity = new AudiovisualEntity();
        audiovisualEntity.setId(1L);
        audiovisualEntity.setTitulo("El Rey León");
        audiovisualEntity.setFechaCreacion(LocalDate.of(1994, 6, 24));
        audiovisualEntity.setCalificacion(5);
        audiovisualEntity.setImagen("src/main/resources/static/images/audiovisual/elreyleon.jpg");

        audiovisualEntity2 = new AudiovisualEntity();
        audiovisualEntity2.setId(2L);
        audiovisualEntity2.setTitulo("Frozen");
        audiovisualEntity2.setFechaCreacion(LocalDate.of(2013, 11, 27));
        audiovisualEntity2.setCalificacion(4);
        audiovisualEntity2.setImagen("src/main/resources/static/images/audiovisual/frozen.jpg");

        audiovisualListDTO = new AudiovisualListDTO(audiovisualEntity.getId(), audiovisualEntity.getTitulo(),
                audiovisualEntity.getImagen(), audiovisualEntity.getDuracion(), audiovisualEntity.getFechaCreacion(), audiovisualEntity.getCalificacion(), audiovisualEntity.getGenero().getNombre());
        audiovisualListDTO2 = new AudiovisualListDTO(audiovisualEntity2.getId(), audiovisualEntity2.getTitulo(),
                audiovisualEntity2.getImagen(), audiovisualEntity2.getDuracion(), audiovisualEntity2.getFechaCreacion(), audiovisualEntity2.getCalificacion(), audiovisualEntity2.getGenero().getNombre());

        audiovisualPrintDTO = new AudiovisualPrintDTO(audiovisualEntity.getId(), audiovisualEntity.getTitulo(), audiovisualEntity.getDescripcion(), audiovisualEntity.getImagen(),
                audiovisualEntity.getDirector(), audiovisualEntity.getFechaCreacion(), audiovisualEntity.getDuracion(), audiovisualEntity.getCalificacion(), "Animación");
        audiovisualPrintDTO2 = new AudiovisualPrintDTO(audiovisualEntity2.getId(), audiovisualEntity2.getTitulo(), audiovisualEntity2.getDescripcion(), audiovisualEntity2.getImagen(),
                audiovisualEntity2.getDirector(), audiovisualEntity2.getFechaCreacion(), audiovisualEntity2.getDuracion(), audiovisualEntity2.getCalificacion(), "Animación");
    }

    @Test
    void getAudiovisuals() throws Exception {
        when(audiovisualService.getAudiovisuals()).thenReturn(List.of(audiovisualListDTO, audiovisualListDTO2));

        mockMvc.perform(get("/audiovisuals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("El Rey León"))
                .andExpect(jsonPath("$[1].titulo").value("Frozen"));

    }

    @Test
    void getAudiovisualById() throws Exception {
        when(audiovisualService.getAudiovisual(eq(audiovisualEntity.getId()))).thenReturn(audiovisualPrintDTO);

        mockMvc.perform(get("/audiovisuals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("El Rey León"));
    }

    @Test
    void deleteAudiovisualById() throws Exception {
        doNothing().when(audiovisualService).deleteAudiovisual(eq(audiovisualEntity.getId()));

        mockMvc.perform(delete("/audiovisuals/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateAudiovisualById() throws Exception {
        doNothing().when(audiovisualService).updateAudiovisual(eq(audiovisualEntity.getId()), eq(audiovisualDTO));

        mockMvc.perform(multipart("/audiovisuals/1")
                        .file((MockMultipartFile) audiovisualDTO.getImagen())
                        .param("titulo", audiovisualDTO.getTitulo())
                        .param("calificacion", String.valueOf(audiovisualDTO.getCalificacion()))
                        .param("fechaCreacion", audiovisualDTO.getFechaCreacion().toString())
                        .param("generoId", String.valueOf(audiovisualDTO.getGeneroId()))
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    void updateGenre() throws Exception {
        doNothing().when(audiovisualService).updateGenre(eq(audiovisualEntity.getId()), eq(1L));

        mockMvc.perform(patch("/audiovisuals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"genreId\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    void addCharacter() throws Exception {
        doNothing().when(audiovisualService).addCharacter(eq(audiovisualEntity.getId()), eq(1L));

        mockMvc.perform(patch("/audiovisuals/1/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"personajeId\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    void removeCharacter() throws Exception {
        doNothing().when(audiovisualService).removeCharacter(eq(audiovisualEntity.getId()), eq(1L));

        mockMvc.perform(patch("/audiovisuals/1/characters/1"))
                .andExpect(status().isOk());
    }

    /*@Test
    void getAudiovisualsByTitle() throws Exception {
        when(audiovisualService.getAudiovisualsByTitle("El Rey León")).thenReturn(List.of(audiovisualPrintDTO));

        mockMvc.perform(get("/audiovisuals?title=El Rey León"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("El Rey León"));
    }

    @Test
    void getAudiovisualsByGenre() throws Exception {
        when(audiovisualService.getAudiovisualsByGenre(1L)).thenReturn(List.of(audiovisualPrintDTO));

        mockMvc.perform(get("/audiovisuals?genre=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("El Rey León"));
    }

    @Test
    void getAudiovisualsByOrder() throws Exception {
        when(audiovisualService.getAudiovisualsByOrder("asc")).thenReturn(List.of(audiovisualPrintDTO, audiovisualPrintDTO2));
        mockMvc.perform(get("/audiovisuals?order=ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("El Rey León"))
                .andExpect(jsonPath("$[1].titulo").value("Frozen"));

        when(audiovisualService.getAudiovisualsByOrder("desc")).thenReturn(List.of(audiovisualPrintDTO2, audiovisualPrintDTO));
        mockMvc.perform(get("/audiovisuals?order=DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Frozen"))
                .andExpect(jsonPath("$[1].titulo").value("El Rey León"));
    } */
}