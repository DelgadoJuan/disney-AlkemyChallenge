package com.alkemy.disney_AlkemyChallenge.Mapper;

import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.CreateUserDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.RegisterDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioEntity registerDTOToUsuarioEntity(RegisterDTO registerDTO);
    UsuarioEntity createUserDTOToUsuarioEntity(CreateUserDTO createUserDTO);
}
