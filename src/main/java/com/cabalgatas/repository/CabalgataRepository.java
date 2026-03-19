package com.cabalgatas.repository;

import com.cabalgatas.entity.Cabalgata;
import com.cabalgatas.entity.EstadoCabalgata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CabalgataRepository extends JpaRepository<Cabalgata, Long> {

    List<Cabalgata> findByFecha(LocalDate fecha);

    @Query("SELECT c FROM Cabalgata c WHERE c.fecha BETWEEN :inicio AND :fin")
    List<Cabalgata> findByFechaBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT c FROM Cabalgata c WHERE c.fecha = :fecha " +
           "AND c.horaInicio < :horaFin AND c.horaFin > :horaInicio " +
           "AND (:cabalgataId IS NULL OR c.id <> :cabalgataId)")
    List<Cabalgata> findSolapadas(
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("cabalgataId") Long cabalgataId);

    List<Cabalgata> findByEstado(EstadoCabalgata estado);
}
