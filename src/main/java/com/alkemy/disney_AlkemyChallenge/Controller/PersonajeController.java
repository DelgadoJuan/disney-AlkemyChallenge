package com.alkemy.disney_AlkemyChallenge.Controller;

import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeListDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;
import com.alkemy.disney_AlkemyChallenge.Service.IPersonajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters")
public class PersonajeController {
    private final IPersonajeService personajeService;

    @Autowired
    public PersonajeController(IPersonajeService personajeService) {
        this.personajeService = personajeService;
    }

    // CRUD simple
    @GetMapping("/{id}")
    public ResponseEntity<PersonajeEntity> getPersonaje(@PathVariable Long id) {
        return personajeService.getCharacter(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PersonajeListDTO>> getAllPersonajes() {
        return new ResponseEntity<>(personajeService.charactersList(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCharacter(@ModelAttribute PersonajeDTO personajeDTO) {
        boolean isAdded = personajeService.addCharacter(personajeDTO);
        if (isAdded) {
            return new ResponseEntity<>("Personaje creado", HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        boolean isDeleted = personajeService.removeCharacter(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCharacter(@PathVariable Long id, @ModelAttribute PersonajeDTO personajeDTO) {
        boolean isUpdated = personajeService.updateCharacter(id, personajeDTO);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    // Requerimientos de Personaje: búsqueda por nombre y filtrado por edad, peso o audiovisual
    @GetMapping(params = "name")
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
    }

    @GetMapping(params = "audiovisualId")
    public ResponseEntity<List<PersonajeEntity>> getCharactersByAudiovisual(@RequestParam Long audiovisualId) {
        return new ResponseEntity<>(personajeService.getCharactersByAudiovisual(audiovisualId), HttpStatus.OK);
    }
}
