package com.alkemy.disney_AlkemyChallenge.Controller;

import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeListDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.GeneroRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IJWTUtilityService;
import com.alkemy.disney_AlkemyChallenge.Service.IPersonajeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = PersonajeController.class)
class PersonajeControllerTest {

    @MockBean
    private IPersonajeService personajeService;
    @MockBean
    private GeneroRepository generoRepository;
    @MockBean
    private IJWTUtilityService jwtUtilityService;

    @Autowired
    private MockMvc mockMvc;

    private PersonajeDTO personajeDTO;
    private PersonajeEntity personajeEntity;
    private PersonajeEntity personajeEntity2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PersonajeController(personajeService))
                .build();

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
        personajeEntity.setId(1L);
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
    }

    @Test
    void getPersonaje() throws Exception {
        when(personajeService.getCharacter(1L)).thenReturn(Optional.of(personajeEntity));

        mockMvc.perform(get("/characters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pato Donald"))
                .andExpect(jsonPath("$.edad").value(85))
                .andExpect(jsonPath("$.peso").value(20))
                .andExpect(jsonPath("$.historia").value("Historia de Pato Donald"))
                .andExpect(jsonPath("$.imagen").value("src/main/resources/static/images/personaje/patodonald.jpg"));
    }

    @Test
    void getAllPersonajes() throws Exception {
        when(personajeService.charactersList()).thenReturn(List.of(
                new PersonajeListDTO(personajeEntity.getNombre(), personajeEntity.getImagen()),
                new PersonajeListDTO(personajeEntity2.getNombre(), personajeEntity2.getImagen())
        ));

        mockMvc.perform(get("/characters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Pato Donald"))
                .andExpect(jsonPath("$[1].nombre").value("Pato enojado"));
    }

    @Test
    void addCharacter() throws Exception {
        when(personajeService.addCharacter(personajeDTO)).thenReturn(true);

        mockMvc.perform(multipart("/characters")
                        .file((MockMultipartFile) personajeDTO.getImagen())
                        .param("nombre", personajeDTO.getNombre())
                        .param("edad", String.valueOf(personajeDTO.getEdad()))
                        .param("peso", String.valueOf(personajeDTO.getPeso()))
                        .param("historia", personajeDTO.getHistoria())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().string("Personaje creado"));
    }

    @Test
    void deleteCharacter() throws Exception {
        when(personajeService.removeCharacter(personajeEntity.getId())).thenReturn(true);

        mockMvc.perform(delete("/characters/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateCharacter() throws Exception {
        when(personajeService.updateCharacter(personajeEntity2.getId(), personajeDTO)).thenReturn(true);

        mockMvc.perform(multipart("/characters/2")
                        .file((MockMultipartFile) personajeDTO.getImagen())
                        .param("nombre", personajeDTO.getNombre())
                        .param("edad", String.valueOf(personajeDTO.getEdad()))
                        .param("peso", String.valueOf(personajeDTO.getPeso()))
                        .param("historia", personajeDTO.getHistoria())
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    void getCharacterByName() throws Exception {
        when(personajeService.getCharactersByName("Pato")).thenReturn(List.of(personajeEntity, personajeEntity2));

        mockMvc.perform(get("/characters?name=Pato"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Pato Donald"))
                .andExpect(jsonPath("$[1].nombre").value("Pato enojado"));
    }

    @Test
    void getCharacterByWeight() throws Exception {
        when(personajeService.getCharactersByWeight(20)).thenReturn(List.of(personajeEntity));

        mockMvc.perform(get("/characters?weight=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Pato Donald"));
    }

    @Test
    void getCharacterByAge() throws Exception {
        when(personajeService.getCharactersByAge(85)).thenReturn(List.of(personajeEntity));

        mockMvc.perform(get("/characters?age=85"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Pato Donald"));
    }

    @Test
    void getCharactersByAudiovisual() throws Exception {
        when(personajeService.getCharactersByAudiovisual(1L)).thenReturn(List.of(personajeEntity));

        mockMvc.perform(get("/characters?audiovisualId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Pato Donald"));
    }
}