package com.alkemy.disney_AlkemyChallenge.Repository;

import com.alkemy.disney_AlkemyChallenge.Entity.AudiovisualEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudiovisualRepository extends JpaRepository<AudiovisualEntity, Long> {
    //@Query(value = "SELECT * FROM genero WHERE titulo LIKE :titulo", nativeQuery = true)
    List<AudiovisualEntity> findByTituloLike(String titulo);
    @Query(value = "SELECT * FROM audiovisual WHERE id_genero = :id_genero", nativeQuery = true)
    List<AudiovisualEntity> findByGenero(@Param("id_genero") Long idGenero);
    List<AudiovisualEntity> findAllByOrderByFechaCreacionAsc();
    List<AudiovisualEntity> findAllByOrderByFechaCreacionDesc();
}
