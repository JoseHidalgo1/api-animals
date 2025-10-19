package com.example.animals.service;

import com.example.animals.model.Animal;
import com.example.animals.repository.AnimalRepository;
import com.example.animals.repository.HabitatRepository;
import com.example.animals.exception.AnimalNotFoundException;
import com.example.animals.exception.AnimalIdAlreadyExistsException;
import com.example.animals.exception.HabitatNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private HabitatRepository habitatRepository;

    // Crear un nuevo animal
    public Animal createAnimal(Animal animal) {
        if (animalRepository.existsById(animal.getId())) {
            throw new AnimalIdAlreadyExistsException("Animal with ID " + animal.getId() + " already exists");
        }
        // Verificar que el habitat existe
        if (animal.getHabitat() != null && animal.getHabitat().getId() != null) {
            habitatRepository.findById(animal.getHabitat().getId())
                    .orElseThrow(() -> new HabitatNotFoundException("Habitat with ID " + animal.getHabitat().getId() + " not found"));
        }
        return animalRepository.save(animal);
    }

    // Obtener todos los animales con filtro opcional
    public List<Animal> getAllAnimals(String isWildFilter) {
        if (isWildFilter == null || isWildFilter.equalsIgnoreCase("all")) {
            return animalRepository.findAll();
        } else if (isWildFilter.equalsIgnoreCase("wild")) {
            return animalRepository.findByIsWild(true);
        } else if (isWildFilter.equalsIgnoreCase("no_wild")) {
            return animalRepository.findByIsWild(false);
        } else {
            return animalRepository.findAll(); // Valor por defecto
        }
    }

    // Obtener animal por ID
    public Animal getAnimalById(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException("Animal with ID " + id + " not found"));
    }

    // Actualizar animal por ID
    public Animal updateAnimal(Long id, Animal animalDetails) {
        Animal existingAnimal = animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException("Animal with ID " + id + " not found"));

        // Si el ID en el cuerpo es diferente al ID del path, verificar que no exista
        if (!animalDetails.getId().equals(id)) {
            if (animalRepository.existsById(animalDetails.getId())) {
                throw new AnimalIdAlreadyExistsException("Animal with ID " + animalDetails.getId() + " already exists");
            }
        }

        // Verificar que el habitat existe si se proporciona
        if (animalDetails.getHabitat() != null && animalDetails.getHabitat().getId() != null) {
            habitatRepository.findById(animalDetails.getHabitat().getId())
                    .orElseThrow(() -> new HabitatNotFoundException("Habitat with ID " + animalDetails.getHabitat().getId() + " not found"));
        }

        // Actualizar los campos
        existingAnimal.setId(animalDetails.getId());
        existingAnimal.setWeight(animalDetails.getWeight());
        existingAnimal.setName(animalDetails.getName());
        existingAnimal.setBirthDateTime(animalDetails.getBirthDateTime());
        existingAnimal.setIsWild(animalDetails.getIsWild());
        existingAnimal.setHabitat(animalDetails.getHabitat());

        return animalRepository.save(existingAnimal);
    }

    // Eliminar animal por ID
    public void deleteAnimal(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException("Animal with ID " + id + " not found"));
        animalRepository.delete(animal);
    }
}
