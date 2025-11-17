package com.example.animals.service;

import com.example.animals.client.KeepersApiClient;
import com.example.animals.dto.AnimalWithKeeperDTO;
import com.example.animals.dto.KeeperDTO;
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

    @Autowired
    private KeepersApiClient keepersApiClient;

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
        // Verificar que el keeper existe si se proporciona
        if (animal.getKeeperId() != null) {
            if (!keepersApiClient.keeperExists(animal.getKeeperId())) {
                throw new IllegalArgumentException("Keeper with ID " + animal.getKeeperId() + " not found in Keepers service");
            }
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

        // Verificar que el keeper existe si se proporciona
        if (animalDetails.getKeeperId() != null) {
            if (!keepersApiClient.keeperExists(animalDetails.getKeeperId())) {
                throw new IllegalArgumentException("Keeper with ID " + animalDetails.getKeeperId() + " not found in Keepers service");
            }
        }

        // Actualizar los campos
        existingAnimal.setId(animalDetails.getId());
        existingAnimal.setWeight(animalDetails.getWeight());
        existingAnimal.setName(animalDetails.getName());
        existingAnimal.setBirthDateTime(animalDetails.getBirthDateTime());
        existingAnimal.setIsWild(animalDetails.getIsWild());
        existingAnimal.setHabitat(animalDetails.getHabitat());
        existingAnimal.setKeeperId(animalDetails.getKeeperId());

        return animalRepository.save(existingAnimal);
    }

    // Eliminar animal por ID
    public void deleteAnimal(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException("Animal with ID " + id + " not found"));
        animalRepository.delete(animal);
    }

    // Consulta personalizada: Buscar animales por peso mínimo
    public List<Animal> getAnimalsByMinWeight(Double minWeight) {
        return animalRepository.findByMinWeight(minWeight);
    }

    // Consulta personalizada: Buscar animales por nombre
    public List<Animal> searchAnimalsByName(String name) {
        return animalRepository.findByNameCustom(name);
    }

    // Asignar un Keeper a un Animal
    public Animal assignKeeperToAnimal(Long animalId, Long keeperId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new AnimalNotFoundException("Animal with ID " + animalId + " not found"));
        
        // Verificar que el keeper existe
        if (!keepersApiClient.keeperExists(keeperId)) {
            throw new IllegalArgumentException("Keeper with ID " + keeperId + " not found in Keepers service");
        }
        
        animal.setKeeperId(keeperId);
        return animalRepository.save(animal);
    }

    // Obtener un animal con información completa de su Keeper
    public AnimalWithKeeperDTO getAnimalWithKeeper(Long animalId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new AnimalNotFoundException("Animal with ID " + animalId + " not found"));
        
        KeeperDTO keeper = null;
        if (animal.getKeeperId() != null) {
            keeper = keepersApiClient.getKeeperById(animal.getKeeperId());
        }
        
        return new AnimalWithKeeperDTO(animal, keeper);
    }

    // Obtener todos los animales con información de sus Keepers
    public List<AnimalWithKeeperDTO> getAllAnimalsWithKeepers() {
        List<Animal> animals = animalRepository.findAll();
        return animals.stream()
                .map(animal -> {
                    KeeperDTO keeper = null;
                    if (animal.getKeeperId() != null) {
                        keeper = keepersApiClient.getKeeperById(animal.getKeeperId());
                    }
                    return new AnimalWithKeeperDTO(animal, keeper);
                })
                .toList();
    }
}
