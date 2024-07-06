package com.alkemy.disney_AlkemyChallenge.Controller;

import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import com.alkemy.disney_AlkemyChallenge.Service.IGeneroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genre")
public class GeneroController {
    private final IGeneroService generoService;

    @Autowired
    public GeneroController(IGeneroService generoService) {
        this.generoService = generoService;
    }

    @GetMapping
    public ResponseEntity<List<GeneroEntity>> getGenres() {
        return new ResponseEntity<>(generoService.getGeneros(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> addGenre(@RequestBody GeneroEntity generoEntity) {
        boolean isCreated = generoService.addGenero(generoEntity);
        if (isCreated) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @RequestBody GeneroEntity generoEntity) {
        boolean isUpdated = generoService.updateGenero(id, generoEntity);
        if (isUpdated) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long id) {
        boolean isDeleted = generoService.deleteGenero(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping(value = "/{id}/audiovisuals/{audiovisualId}")
    public ResponseEntity<?> deleteAudiovisualFromGenre(@PathVariable("id") Long id,
                                               @PathVariable("audiovisualId") Long audiovisualId) {
        boolean isDeleted = generoService.deleteAudiovisualFromGenre(id, audiovisualId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
