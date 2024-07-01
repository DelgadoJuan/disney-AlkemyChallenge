package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.DTO.LoginDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.ResponseDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.UsuarioEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.UsuarioRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IAuthService;
import com.alkemy.disney_AlkemyChallenge.Service.IJWTUtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {
    private final UsuarioRepository UsuarioRepository;
    private final IJWTUtilityService jwtUtilityService;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AuthServiceImpl(UsuarioRepository UsuarioRepository, IJWTUtilityService jwtUtilityService, UsuarioRepository usuarioRepository) {
        this.UsuarioRepository = UsuarioRepository;
        this.jwtUtilityService = jwtUtilityService;
        this.usuarioRepository = usuarioRepository;
        this.encoder = new BCryptPasswordEncoder(12);
    }

    @Override
    public HashMap<String, String> login(LoginDTO loginDTO) throws Exception {
        try {
            HashMap<String, String> jwt = new HashMap<>();
            Optional<UsuarioEntity> usuario = UsuarioRepository.findByEmail(loginDTO.getEmail());

            if (usuario.isEmpty()) {
                jwt.put("Error", "Usuario no registrado");
                return jwt;
            }

            // Verificar la contraseña
            if (verifyPassword(loginDTO.getPassword(), usuario.get().getPassword())) {
                jwt.put("JWT", jwtUtilityService.generateJWT(usuario.get().getId()));
            } else {
                jwt.put("Error", "Autenticación fallida");
            }

            return jwt;
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    public ResponseDTO register(UsuarioEntity usuario) throws Exception {
        try {
            ResponseDTO responseDTO = new ResponseDTO();
            List<UsuarioEntity> getAllUsers = usuarioRepository.findAll();
            for (UsuarioEntity usuarioEntity : getAllUsers) {
                if (usuarioEntity.getEmail().equals(usuario.getEmail())) {
                    throw new Exception(usuario.getEmail() + " ya esta registrado");
                }
            }

            usuario.setPassword(encoder.encode(usuario.getPassword()));
            usuarioRepository.save(usuario);
            responseDTO.setMessage("Usuario registrado");

            return responseDTO;
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    private boolean verifyPassword(String enteredPassword, String StoredPassword) {
        return encoder.matches(enteredPassword, StoredPassword);
    }
}
