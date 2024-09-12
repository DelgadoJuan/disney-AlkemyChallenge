package com.alkemy.disney_AlkemyChallenge.Controller;

import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualListDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualPrintDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import com.alkemy.disney_AlkemyChallenge.Service.IAudiovisualService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/audiovisuals")
public class AudiovisualController {
    private final IAudiovisualService audiovisualService;

    @Autowired
    public AudiovisualController(IAudiovisualService audiovisualService) {
        this.audiovisualService = audiovisualService;
    }

    @GetMapping
    public ResponseEntity<List<AudiovisualListDTO>> getAudiovisuals() {
        return ResponseEntity.ok(audiovisualService.getAudiovisuals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AudiovisualEntity> getAudiovisualById(@PathVariable Long id) {
        AudiovisualEntity audiovisual = audiovisualService.getAudiovisual(id);
        return ResponseEntity.ok(audiovisual);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAudiovisual(@ModelAttribute @Valid AudiovisualDTO audiovisual) {
        boolean isAdded = audiovisualService.addAudiovisual(audiovisual);
        return isAdded ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAudiovisualById(@PathVariable Long id) {
        boolean isDeleted = audiovisualService.deleteAudiovisual(id);
        return isDeleted ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAudiovisualById(@PathVariable Long id, @ModelAttribute @Valid AudiovisualDTO audiovisual) {
        boolean isUpdated = audiovisualService.updateAudiovisual(id, audiovisual);
        return isUpdated ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @RequestBody Map<String, Long> genre) {
        Long genreId = genre.get("genreId");
        boolean isUpdated = audiovisualService.updateGenre(id, genreId);
        return isUpdated ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PatchMapping("/{id}/characters")
    public ResponseEntity<?> addCharacter(@PathVariable Long id, @RequestBody Map<String, Long> personaje) {
        Long personajeId = personaje.get("personajeId");
        boolean isAdded = audiovisualService.addCharacter(id, personajeId);
        return isAdded ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PatchMapping("/{id}/characters/{personajeId}")
    public ResponseEntity<?> removeCharacter(@PathVariable("id") Long id, @PathVariable("personajeId") Long personajeId) {
        boolean isRemoved = audiovisualService.removeCharacter(id, personajeId);
        return isRemoved ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    // Búsqueda y filtrado

    @GetMapping(params = "title")
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
    }
}
