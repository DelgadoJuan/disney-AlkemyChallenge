package com.alkemy.disney_AlkemyChallenge.Service;

import com.alkemy.disney_AlkemyChallenge.DTO.LoginDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.ResponseDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.UsuarioEntity;

import java.util.HashMap;

public interface IAuthService {
    HashMap<String, String> login(LoginDTO loginDTO) throws Exception;
    ResponseDTO register(UsuarioEntity usuario) throws Exception;
}
