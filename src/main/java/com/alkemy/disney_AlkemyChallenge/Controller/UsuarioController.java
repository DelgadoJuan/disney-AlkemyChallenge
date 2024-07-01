package com.alkemy.disney_AlkemyChallenge.Controller;

import com.alkemy.disney_AlkemyChallenge.DTO.LoginDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.ResponseDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.UsuarioEntity;
import com.alkemy.disney_AlkemyChallenge.Service.IAuthService;
import com.alkemy.disney_AlkemyChallenge.Service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UsuarioController {

    final IUsuarioService usuarioService;

    @Autowired
    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/findAll")
    private ResponseEntity<List<UsuarioEntity>> findAll(){
        return new ResponseEntity<>(usuarioService.findAllUsers(), HttpStatus.OK);
    }

}
