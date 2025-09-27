package com.example.animals.controller;

import com.example.animals.model.Animal;
import com.example.animals.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animals")
@CrossOrigin(origins = "*")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    // POST /animals - Crear un nuevo animal
    @PostMapping
    public ResponseEntity<Animal> createAnimal(@Valid @RequestBody Animal animal) {
        Animal savedAnimal = animalService.createAnimal(animal);
        return new ResponseEntity<>(savedAnimal, HttpStatus.CREATED);
    }

    // GET /animals - Obtener todos los animales con filtro opcional
    @GetMapping
    public ResponseEntity<List<Animal>> getAllAnimals(
            @RequestParam(value = "is_wild", required = false, defaultValue = "all") String isWild) {
        List<Animal> animals = animalService.getAllAnimals(isWild);
        return new ResponseEntity<>(animals, HttpStatus.OK);
    }

    // GET /animals/{id} - Obtener animal por ID
    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable Long id) {
        Animal animal = animalService.getAnimalById(id);
        return new ResponseEntity<>(animal, HttpStatus.OK);
    }

    // PUT /animals/{id} - Actualizar animal por ID
    @PutMapping("/{id}")
    public ResponseEntity<Animal> updateAnimal(
            @PathVariable Long id,
            @Valid @RequestBody Animal animalDetails) {
        Animal updatedAnimal = animalService.updateAnimal(id, animalDetails);
        return new ResponseEntity<>(updatedAnimal, HttpStatus.OK);
    }

    // DELETE /animals/{id} - Eliminar animal por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        animalService.deleteAnimal(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}