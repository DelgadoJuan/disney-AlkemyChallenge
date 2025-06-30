package com.alkemy.disney_AlkemyChallenge.Controller;

import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualListDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualPrintDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualAdminDTO;
import com.alkemy.disney_AlkemyChallenge.Service.IAudiovisualService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/audiovisuals")
@Tag(name = "Audiovisual", description = "API de gestión de audiovisuales")
public class AudiovisualController {
    private final IAudiovisualService audiovisualService;

    @Autowired
    public AudiovisualController(IAudiovisualService audiovisualService) {
        this.audiovisualService = audiovisualService;
    }

    @Operation(summary = "Obtener todos los audiovisuales", description = "Retorna una lista de todos los audiovisuales disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de audiovisuales encontrada",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = AudiovisualListDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<AudiovisualListDTO>> getAudiovisuals() {
        return ResponseEntity.ok(audiovisualService.getAudiovisuals());
    }

    @Operation(summary = "Obtener todos los audiovisuales para administración", description = "Retorna una lista de todos los audiovisuales con director y descripción para la sección de administración")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de audiovisuales para administración encontrada",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = AudiovisualAdminDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AudiovisualAdminDTO>> getAudiovisualsForAdmin() {
        return ResponseEntity.ok(audiovisualService.getAudiovisualsForAdmin());
    }

    @Operation(summary = "Obtener un audiovisual por ID", description = "Retorna un audiovisual específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audiovisual encontrado",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = AudiovisualPrintDTO.class))),
        @ApiResponse(responseCode = "404", description = "Audiovisual no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AudiovisualPrintDTO> getAudiovisualById(
            @Parameter(description = "ID del audiovisual a buscar") @PathVariable Long id) {
        AudiovisualPrintDTO audiovisual = audiovisualService.getAudiovisual(id);
        return new ResponseEntity<>(audiovisual, HttpStatus.OK);
    }

    @Operation(summary = "Crear un nuevo audiovisual", description = "Crea un nuevo audiovisual con la información proporcionada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Audiovisual creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addAudiovisual(
            @Parameter(description = "Datos del audiovisual a crear") 
            @ModelAttribute @Valid AudiovisualDTO audiovisual) {
        audiovisualService.addAudiovisual(audiovisual);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar un audiovisual", description = "Elimina un audiovisual específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audiovisual eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Audiovisual no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAudiovisualById(
            @Parameter(description = "ID del audiovisual a eliminar") @PathVariable Long id) {
        audiovisualService.deleteAudiovisual(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Actualizar un audiovisual", description = "Actualiza la información de un audiovisual existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audiovisual actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Audiovisual no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAudiovisualById(
            @Parameter(description = "ID del audiovisual a actualizar") @PathVariable Long id,
            @Parameter(description = "Nuevos datos del audiovisual") @ModelAttribute @Valid AudiovisualDTO audiovisual) {
        audiovisualService.updateAudiovisual(id, audiovisual);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Actualizar género de un audiovisual", description = "Actualiza el género de un audiovisual específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Género actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Audiovisual o género no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateGenre(
            @Parameter(description = "ID del audiovisual") @PathVariable Long id,
            @Parameter(description = "ID del nuevo género") @RequestBody Map<String, Long> genre) {
        Long genreId = genre.get("genreId");
        audiovisualService.updateGenre(id, genreId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Agregar personaje a un audiovisual", description = "Agrega un personaje a un audiovisual específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personaje agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Audiovisual o personaje no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/characters")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCharacter(
            @Parameter(description = "ID del audiovisual") @PathVariable Long id,
            @Parameter(description = "ID del personaje a agregar") @RequestBody Map<String, Long> personaje) {
        Long personajeId = personaje.get("personajeId");
        audiovisualService.addCharacter(id, personajeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Eliminar personaje de un audiovisual", description = "Elimina un personaje de un audiovisual específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personaje eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Audiovisual o personaje no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/characters/{personajeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeCharacter(
            @Parameter(description = "ID del audiovisual") @PathVariable("id") Long id,
            @Parameter(description = "ID del personaje a eliminar") @PathVariable("personajeId") Long personajeId) {
        audiovisualService.removeCharacter(id, personajeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Obtener audiovisuales destacados", description = "Retorna una lista de los audiovisuales más destacados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de audiovisuales destacados encontrada",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = AudiovisualListDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/featured")
    public ResponseEntity<List<AudiovisualListDTO>> getFeaturedAudiovisuals() {
        return ResponseEntity.ok(audiovisualService.getTopAudiovisuals(5));
    }

    // Búsqueda y filtrado

    /*@GetMapping(params = "title")
    public ResponseEntity<List<AudiovisualPrintDTO>> getAudiovisualsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(audiovisualService.getAudiovisualsByTitle(title));
    }

    @GetMapping(params = "genre")
    public ResponseEntity<List<AudiovisualPrintDTO>> getAudiovisualsByGenre(@RequestParam String genre) {
        try {
            // Convertir el parámetro a Long
            Long id_genre = Long.parseLong(genre);

            // Validar que el número sea positivo
            if (id_genre <= 0) {
                return ResponseEntity.badRequest().body(null); // Devuelve error si no es positivo
            }

            // Llamar al servicio si la validación pasa
            return ResponseEntity.ok(audiovisualService.getAudiovisualsByGenre(id_genre));
        } catch (NumberFormatException e) {
            // Manejar el caso en que el parámetro no es un número
            return ResponseEntity.badRequest().body(null); // Devuelve error si no es un número válido
        }
    }

    @GetMapping(params = "order")
    public ResponseEntity<List<AudiovisualPrintDTO>> getAudiovisualsByOrder(@RequestParam String order) {
        if (!"asc".equalsIgnoreCase(order) && !"desc".equalsIgnoreCase(order)) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(audiovisualService.getAudiovisualsByOrder(order.toLowerCase(Locale.ROOT)));
    } */
}
