package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.Entity.UsuarioEntity;
import com.alkemy.disney_AlkemyChallenge.Repository.UsuarioRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioEntity> findAllUsers() {
        return usuarioRepository.findAll();
    }
}
