package com.alkemy.disney_AlkemyChallenge.Service;

import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.LoginDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.ResponseDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.RegisterDTO;

import java.util.HashMap;

public interface IAuthService {
    HashMap<String, String> login(LoginDTO loginDTO) throws Exception;
    ResponseDTO register(RegisterDTO usuario) throws Exception;
}
