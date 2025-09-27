package com.example.animals.repository;

import com.example.animals.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    // Buscar por tipo de animal (salvaje o domestico)
    List<Animal> findByIsWild(Boolean isWild);

    // Verificar si existe un animal con el ID dado
    boolean existsById(Long id);

    // Buscar animales por nombre
    List<Animal> findByNameContainingIgnoreCase(String name);
}