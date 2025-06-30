package com.alkemy.disney_AlkemyChallenge.Service;

import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.CreateUserDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.RegisterDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.UpdatePasswordDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.UsuarioEntity;

import java.util.List;

public interface IUsuarioService {
     List<UsuarioEntity> findAllUsers();
     UsuarioEntity findUserById(Long id);
     void updatePassword(Long id, UpdatePasswordDTO updatePasswordDTO);
     UsuarioEntity createUser(CreateUserDTO usuario);
     UsuarioEntity updateUser(Long id, UsuarioEntity usuario);
     void deleteUser(Long id);
}
