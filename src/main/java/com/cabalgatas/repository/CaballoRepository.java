package com.cabalgatas.repository;

import com.cabalgatas.entity.Caballo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaballoRepository extends JpaRepository<Caballo, Long> {

    List<Caballo> findByDisponibleTrue();
}
