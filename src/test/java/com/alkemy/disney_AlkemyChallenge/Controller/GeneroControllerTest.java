package com.alkemy.disney_AlkemyChallenge.Controller;

import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.GeneroRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IGeneroService;
import com.alkemy.disney_AlkemyChallenge.Service.Impl.JWTUtilityServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GeneroController.class)
class GeneroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String directory = "src/main/resources/static/images/genero/";

    @MockBean
    private GeneroRepository generoRepository;

    @MockBean
    private JWTUtilityServiceImpl jwtUtilityService;

    GeneroDTO generoDTO;
    GeneroEntity generoEntity;

    @MockBean
    private IGeneroService generoService;

    @BeforeEach
    void setup() {
        // Inicializar el MockMvc
        mockMvc = MockMvcBuilders
                .standaloneSetup(new GeneroController(generoService))
                .build();

        // Crear un objeto DTO común para las pruebas
        generoDTO = new GeneroDTO();
        generoDTO.setNombre("Acción");

        // Crear una entidad común para las pruebas
        generoEntity = new GeneroEntity();
        generoEntity.setId(1L);
        generoEntity.setNombre("Acción");

        // Mock para métodos comunes
        when(generoService.addGenero(any(GeneroDTO.class))).thenReturn(true);
        when(generoService.updateGenero(eq(1L), any(GeneroDTO.class))).thenReturn(true);
        when(generoService.getGeneros()).thenReturn(List.of(generoEntity));
    }

    @Test
    void getGenres() throws Exception {
        // Preparar datos de prueba
        GeneroEntity genero = new GeneroEntity();
        List<GeneroEntity> generos = List.of(genero);

        // Configurar el mock para el servicio
        when(generoService.getGeneros()).thenReturn(generos);

        // Realizar la solicitud GET
        mockMvc.perform(get("/genre"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void addGenre() throws Exception {
        // Crear un MockMultipartFile para simular la imagen
        MockMultipartFile imageFile = new MockMultipartFile(
                "imagen", "drama.jpg", "image/jpeg", "imagen de prueba".getBytes()
        );

        // Realizar la solicitud POST
        mockMvc.perform(multipart("/genre")
                        .file(imageFile)
                        .param("nombre", generoDTO.getNombre()) // Usar el DTO preparado
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }

    @Test
    void updateGenre() throws Exception {
        // Crear un MockMultipartFile para simular la imagen
        MockMultipartFile imageFile = new MockMultipartFile(
                "imagen", "accion.jpg", "image/jpeg", "contenido de la imagen".getBytes()
        );

        // Realizar la solicitud PUT usando multipart
        mockMvc.perform(multipart("/genre/1")
                        .file(imageFile)
                        .param("nombre", generoDTO.getNombre()) // Usar el DTO preparado
                        .with(request -> {
                            request.setMethod("PUT"); // Forzar el metodo PUT
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    void deleteGenre() throws Exception {
        when(generoService.deleteGenero(1L)).thenReturn(true);

        mockMvc.perform(delete("/genre/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAudiovisualFromGenre() throws Exception {
        when(generoService.deleteAudiovisualFromGenre(1L, 2L)).thenReturn(true);

        mockMvc.perform(patch("/genre/1/audiovisuals/2"))
                .andExpect(status().isOk());
    }
}