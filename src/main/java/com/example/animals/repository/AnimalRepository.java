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
}