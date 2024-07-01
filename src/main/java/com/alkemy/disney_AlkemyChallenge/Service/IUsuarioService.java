package com.alkemy.disney_AlkemyChallenge.Service;

import com.alkemy.disney_AlkemyChallenge.Entity.UsuarioEntity;

import java.util.List;

public interface IUsuarioService {
    public List<UsuarioEntity> findAllUsers();
}
