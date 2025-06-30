package com.alkemy.disney_AlkemyChallenge.Mapper;

import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Genero.GeneroResponseDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GeneroMapper {
    @Mapping(target = "imagen", ignore = true)
    GeneroEntity generoDTOtoGeneroEntity(GeneroDTO generoDTO);
    GeneroResponseDTO generoEntityToGeneroResponseDTO(GeneroEntity generoEntity);
}
