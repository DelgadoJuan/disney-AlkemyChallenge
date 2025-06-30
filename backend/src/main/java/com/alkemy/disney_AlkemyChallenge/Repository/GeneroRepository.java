package com.alkemy.disney_AlkemyChallenge.Repository;

import com.alkemy.disney_AlkemyChallenge.Entity.GeneroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<GeneroEntity, Long> {
}
