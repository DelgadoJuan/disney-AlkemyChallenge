package com.alkemy.disney_AlkemyChallenge.Controller;

import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeListDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajePrintDTO;
import com.alkemy.disney_AlkemyChallenge.Service.IPersonajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class PersonajeController {
    private final IPersonajeService personajeService;

    @Operation(summary = "Obtener un personaje por su ID", description = "Obtiene un personaje específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personaje obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Personaje no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonajePrintDTO> getPersonaje(@PathVariable Long id) {
        return new ResponseEntity<>(personajeService.getCharacter(id)
                .orElseThrow(() -> new RuntimeException("Personaje no encontrado")), HttpStatus.OK);
    }

    @Operation(summary = "Obtener todos los personajes", description = "Obtiene todos los personajes disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personajes obtenidos exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<PersonajeListDTO>> getCharactersList() {
        return new ResponseEntity<>(personajeService.charactersList(), HttpStatus.OK);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PersonajePrintDTO>> getAllPersonajes() {
        return new ResponseEntity<>(personajeService.getAllCharacters(), HttpStatus.OK);
    }

    @Operation(summary = "Agregar un nuevo personaje", description = "Agrega un nuevo personaje a la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Personaje agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCharacter(@ModelAttribute @Valid PersonajeDTO personajeDTO) {
        boolean isAdded = personajeService.addCharacter(personajeDTO);
        if (isAdded) {
            return new ResponseEntity<>("Personaje creado", HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Operation(summary = "Eliminar un personaje", description = "Elimina un personaje existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personaje eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Personaje no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        boolean isDeleted = personajeService.removeCharacter(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Operation(summary = "Actualizar un personaje", description = "Actualiza un personaje existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personaje actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Personaje no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateCharacter(@PathVariable Long id, @ModelAttribute @Valid PersonajeDTO personajeDTO) {
        boolean isUpdated = personajeService.updateCharacter(id, personajeDTO);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Operation(summary = "Obtener personajes por ID de audiovisual", description = "Obtiene personajes asociados a un audiovisual específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personajes obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Audiovisual no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(params = "audiovisualId")
    public ResponseEntity<List<PersonajeListDTO>> getCharactersByAudiovisual(@RequestParam Long audiovisualId) {
        return new ResponseEntity<>(personajeService.getCharactersByAudiovisual(audiovisualId), HttpStatus.OK);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<PersonajeListDTO>> getFeaturedCharacters() {
        return ResponseEntity.ok(personajeService.getTopCharacters(5));
    }

    // Requerimientos de Personaje: búsqueda por nombre y filtrado por edad, peso o audiovisual
    /*@GetMapping(params = "name")
    public ResponseEntity<List<PersonajeEntity>> getCharacterByName(@RequestParam String name) {
        return new ResponseEntity<>(personajeService.getCharactersByName(name), HttpStatus.OK);
    }

    @GetMapping(params = "weight")
    public ResponseEntity<List<PersonajeEntity>> getCharacterByWeight(@RequestParam double weight) {
        return new ResponseEntity<>(personajeService.getCharactersByWeight(weight), HttpStatus.OK);
    }

    @GetMapping(params = "age")
    public ResponseEntity<List<PersonajeEntity>> getCharacterByAge(@RequestParam int age) {
        return new ResponseEntity<>(personajeService.getCharactersByAge(age), HttpStatus.OK);
    } */
}
