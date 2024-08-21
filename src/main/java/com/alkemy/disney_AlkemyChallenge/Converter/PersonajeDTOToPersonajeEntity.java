package com.alkemy.disney_AlkemyChallenge.Converter;

import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PersonajeDTOToPersonajeEntity implements Converter<PersonajeDTO, PersonajeEntity> {
    @Override
    public PersonajeEntity convert(PersonajeDTO personajeDTO) {
        PersonajeEntity personajeEntity = new PersonajeEntity();
        personajeEntity.setNombre(personajeDTO.getNombre());
        personajeEntity.setEdad(personajeDTO.getEdad());
        personajeEntity.setPeso(personajeDTO.getPeso());
        personajeEntity.setHistoria(personajeDTO.getHistoria());
        return personajeEntity;
    }
}
