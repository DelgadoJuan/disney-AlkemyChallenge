package com.alkemy.disney_AlkemyChallenge.Controller;

import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroResponseDTO;
import com.alkemy.disney_AlkemyChallenge.Service.IGeneroService;
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
@RequestMapping("/genre")
@RequiredArgsConstructor
public class GeneroController {
    private final IGeneroService generoService;

    @Operation(summary = "Obtener todos los géneros", description = "Obtiene todos los géneros disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Géneros obtenidos exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<GeneroResponseDTO>> getGenres() {
        return new ResponseEntity<>(generoService.getGeneros(), HttpStatus.OK);
    }

    @Operation(summary = "Agregar un nuevo género", description = "Agrega un nuevo género a la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Género agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addGenre(@ModelAttribute @Valid GeneroDTO generoDTO) {
        boolean isCreated = generoService.addGenero(generoDTO);
        if (isCreated) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Actualizar un género", description = "Actualiza un género existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Género actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Género no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @ModelAttribute @Valid GeneroDTO generoDTO) {
        boolean isUpdated = generoService.updateGenero(id, generoDTO);
        if (isUpdated) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Eliminar un género", description = "Elimina un género existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Género eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Género no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteGenre(@PathVariable Long id) {
        boolean isDeleted = generoService.deleteGenero(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Eliminar un audiovisual de un género", description = "Elimina un audiovisual de un género existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audiovisual eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Audiovisual o género no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping(value = "/{id}/audiovisuals/{audiovisualId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAudiovisualFromGenre(@PathVariable("id") Long id,
                                               @PathVariable("audiovisualId") Long audiovisualId) {
        boolean isDeleted = generoService.deleteAudiovisualFromGenre(id, audiovisualId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
