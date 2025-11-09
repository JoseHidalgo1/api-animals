package com.example.animals.repository;

import com.example.animals.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findByIsWild(Boolean isWild);

    boolean existsById(Long id);

    boolean existsByHabitatId(Integer habitatId);

    List<Animal> findByNameContainingIgnoreCase(String name);

    // Consulta personalizada 1: Buscar animales por peso mínimo
    @Query("SELECT a FROM Animal a WHERE a.weight >= :minWeight ORDER BY a.weight DESC")
    List<Animal> findByMinWeight(@Param("minWeight") Double minWeight);

    // Consulta personalizada 2: Buscar animales por nombre parcial (usando JPQL personalizado)
    @Query("SELECT a FROM Animal a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Animal> findByNameCustom(@Param("name") String name);
}