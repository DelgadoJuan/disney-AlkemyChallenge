package com.alkemy.disney_AlkemyChallenge.Controller;

import com.alkemy.disney_AlkemyChallenge.DTO.LoginDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.ResponseDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.UsuarioEntity;
import com.alkemy.disney_AlkemyChallenge.Service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {
    final IAuthService authService;

    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    private ResponseEntity<ResponseDTO> register(@RequestBody UsuarioEntity usuario) throws Exception {
        return new ResponseEntity<>(authService.register(usuario), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    private ResponseEntity<HashMap<String, String>> login(@RequestBody LoginDTO loginDTO) throws Exception {
        HashMap<String, String> login = authService.login(loginDTO);
        if (login.containsKey("jwt")) {
            return new ResponseEntity<>(login, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(login, HttpStatus.UNAUTHORIZED);
        }
    }
}
