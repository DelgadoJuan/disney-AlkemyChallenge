package com.alkemy.disney_AlkemyChallenge.Repository;

import com.alkemy.disney_AlkemyChallenge.Entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);
}
