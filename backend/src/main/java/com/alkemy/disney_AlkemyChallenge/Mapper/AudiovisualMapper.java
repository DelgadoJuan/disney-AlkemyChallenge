package com.alkemy.disney_AlkemyChallenge.Mapper;

import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualListDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualPrintDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Audiovisual.AudiovisualAdminDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import lombok.RequiredArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class AudiovisualMapper {
    @Mapping(target = "genero", ignore = true)
    @Mapping(target = "imagen", ignore = true)
    public abstract AudiovisualEntity audiovisualDTOtoAudiovisualEntity(AudiovisualDTO audiovisualDTO);
    public abstract AudiovisualPrintDTO audiovisualEntityToAudiovisualPrintDTO(AudiovisualEntity audiovisualEntity);
    public abstract AudiovisualListDTO audiovisualEntityToAudiovisualListDTO(AudiovisualEntity audiovisualEntity);
    public abstract AudiovisualAdminDTO audiovisualEntityToAudiovisualAdminDTO(AudiovisualEntity audiovisualEntity);

    @AfterMapping
    void setGenero(AudiovisualEntity entity, @MappingTarget AudiovisualPrintDTO dto) {
        if (entity.getGenero() != null) {
            dto.setNombreGenero(entity.getGenero().getNombre());
        } else {
            dto.setNombreGenero(null);
        }
    }

    @AfterMapping
    void setGenero(AudiovisualEntity entity, @MappingTarget AudiovisualListDTO dto) {
        if (entity.getGenero() != null) {
            dto.setNombreGenero(entity.getGenero().getNombre());
        } else {
            dto.setNombreGenero(null);
        }
    }

    @AfterMapping
    void setGenero(AudiovisualEntity entity, @MappingTarget AudiovisualAdminDTO dto) {
        if (entity.getGenero() != null) {
            dto.setNombreGenero(entity.getGenero().getNombre());
        } else {
            dto.setNombreGenero(null);
        }
    }
}
