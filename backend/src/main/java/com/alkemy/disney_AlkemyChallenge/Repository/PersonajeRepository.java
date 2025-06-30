package com.alkemy.disney_AlkemyChallenge.Repository;

import com.alkemy.disney_AlkemyChallenge.Entity.PersonajeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonajeRepository extends JpaRepository<PersonajeEntity, Long> {
    List<PersonajeEntity> findByNombre(String name);
    List<PersonajeEntity> findByEdad(int age);
    List<PersonajeEntity> findByPeso(double weight);
    @Query(value = "SELECT p.* FROM personaje p " +
            "JOIN personaje_audiovisual pa ON p.id = pa.id_personaje " +
            "WHERE pa.id_audiovisual = :audiovisualId", nativeQuery = true)
    List<PersonajeEntity> findByAudiovisuales(@Param("audiovisualId") Long audiovisualId);
}
