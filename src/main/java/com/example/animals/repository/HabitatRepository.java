package com.example.animals.repository;

import com.example.animals.model.Habitat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitatRepository extends JpaRepository<Habitat, Integer> {
    List<Habitat> findByIsCovered(boolean isCovered);
}
