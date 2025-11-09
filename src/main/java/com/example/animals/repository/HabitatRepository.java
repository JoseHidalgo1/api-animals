package com.example.animals.repository;

import com.example.animals.model.Habitat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitatRepository extends JpaRepository<Habitat, Integer> {
    List<Habitat> findByIsCovered(boolean isCovered);

    // Consulta personalizada: Obtener Habitat con sus Animals (Maestro-Detalle)
    @Query("SELECT DISTINCT h FROM Habitat h LEFT JOIN FETCH h.animals WHERE h.id = :id")
    Optional<Habitat> findByIdWithAnimals(@Param("id") Integer id);

    // Consulta personalizada: Obtener todos los Habitats con sus Animals
    @Query("SELECT DISTINCT h FROM Habitat h LEFT JOIN FETCH h.animals")
    List<Habitat> findAllWithAnimals();
}
