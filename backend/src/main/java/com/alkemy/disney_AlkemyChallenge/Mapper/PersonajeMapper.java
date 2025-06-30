package com.alkemy.disney_AlkemyChallenge.Mapper;

import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeAudiovisualDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajeListDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Personaje.PersonajePrintDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.stream.Collectors;

@Mapper(componentModel = "Spring")
public interface PersonajeMapper {
    @Mapping(target = "imagen", ignore = true)
    PersonajeEntity personajeDTOtoPersonajeEntity(PersonajeDTO personajeDTO);
    PersonajeListDTO personajeEntityToPersonajeListDTO(PersonajeEntity personajeEntity);
    PersonajePrintDTO personajeEntityToPersonajePrintDTO(PersonajeEntity personajeEntity);
    PersonajeAudiovisualDTO personajeEntityToPersonajeAudiovisualDTO(AudiovisualEntity audiovisualEntity);

    @AfterMapping
    default void mapAudiovisuales(PersonajeEntity entity, @MappingTarget PersonajePrintDTO dto) {
        if (entity.getAudiovisuales() != null) {
            dto.setAudiovisuales(
                    entity.getAudiovisuales().stream()
                            .map(this::personajeEntityToPersonajeAudiovisualDTO)
                            .collect(Collectors.toList())
            );
        }
    }
}
