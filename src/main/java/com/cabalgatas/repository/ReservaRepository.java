package com.cabalgatas.repository;

import com.cabalgatas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByCabalgataId(Long cabalgataId);

    @Query("SELECT COALESCE(SUM(r.cantidadPersonas), 0) FROM Reserva r WHERE r.cabalgata.id = :cabalgataId")
    int sumCantidadPersonasByCabalgataId(@Param("cabalgataId") Long cabalgataId);
}
