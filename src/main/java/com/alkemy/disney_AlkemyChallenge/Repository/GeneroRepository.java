package com.alkemy.disney_AlkemyChallenge.Repository;

import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneroRepository extends JpaRepository<GeneroEntity, Long> {
}
