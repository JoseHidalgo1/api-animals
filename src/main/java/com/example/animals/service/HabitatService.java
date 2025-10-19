package com.example.animals.service;

import com.example.animals.model.Habitat;
import com.example.animals.repository.HabitatRepository;
import com.example.animals.repository.AnimalRepository;
import com.example.animals.exception.HabitatNotFoundException;
import com.example.animals.exception.HabitatIdAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitatService {

    @Autowired
    private HabitatRepository habitatRepository;

    @Autowired
    private AnimalRepository animalRepository;

    public Habitat createHabitat(Habitat habitat) {
        if (habitatRepository.existsById(habitat.getId())) {
            throw new HabitatIdAlreadyExistsException("Habitat with ID " + habitat.getId() + " already exists");
        }
        return habitatRepository.save(habitat);
    }

    public List<Habitat> getAllHabitats(String isCoveredFilter) {
        if (isCoveredFilter == null || isCoveredFilter.equalsIgnoreCase("all")) {
            return habitatRepository.findAll();
        } else if (isCoveredFilter.equalsIgnoreCase("true")) {
            return habitatRepository.findByIsCovered(true);
        } else if (isCoveredFilter.equalsIgnoreCase("false")) {
            return habitatRepository.findByIsCovered(false);
        } else {
            return habitatRepository.findAll();
        }
    }

    public Habitat getHabitatById(Integer id) {
        return habitatRepository.findById(id)
                .orElseThrow(() -> new HabitatNotFoundException("Habitat with ID " + id + " not found"));
    }

    public Habitat updateHabitat(Integer id, Habitat habitatDetails) {
        Habitat existingHabitat = habitatRepository.findById(id)
                .orElseThrow(() -> new HabitatNotFoundException("Habitat with ID " + id + " not found"));

        existingHabitat.setName(habitatDetails.getName());
        existingHabitat.setArea(habitatDetails.getArea());
        existingHabitat.setEstablishedDate(habitatDetails.getEstablishedDate());
        existingHabitat.setIsCovered(habitatDetails.getIsCovered());

        return habitatRepository.save(existingHabitat);
    }

    public void deleteHabitat(Integer id) {
        Habitat habitat = habitatRepository.findById(id)
                .orElseThrow(() -> new HabitatNotFoundException("Habitat with ID " + id + " not found"));

        if (animalRepository.existsByHabitatId(id)) {
            throw new IllegalStateException("Cannot delete habitat because it is associated to one or more animals.");
        }

        habitatRepository.delete(habitat);
    }
}
