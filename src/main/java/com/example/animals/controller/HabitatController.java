package com.example.animals.controller;

import com.example.animals.model.Habitat;
import com.example.animals.service.HabitatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habitats")
@CrossOrigin(origins = "*")
public class HabitatController {

    @Autowired
    private HabitatService habitatService;

    @PostMapping
    public ResponseEntity<Habitat> createHabitat(@Valid @RequestBody(required = false) Habitat habitat) {
        if (habitat == null) {
            throw new IllegalArgumentException("Request body is required for creating a habitat");
        }
        Habitat savedHabitat = habitatService.createHabitat(habitat);
        return new ResponseEntity<>(savedHabitat, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Habitat>> getAllHabitats(
            @RequestParam(value = "is_covered", required = false, defaultValue = "all") String isCovered) {
        List<Habitat> habitats = habitatService.getAllHabitats(isCovered);
        return new ResponseEntity<>(habitats, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habitat> getHabitatById(@PathVariable Integer id) {
        Habitat habitat = habitatService.getHabitatById(id);
        return new ResponseEntity<>(habitat, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Habitat> updateHabitat(
            @PathVariable Integer id,
            @Valid @RequestBody(required = false) Habitat habitatDetails) {
        if (habitatDetails == null) {
            throw new IllegalArgumentException("Request body is required for updating a habitat");
        }
        Habitat updatedHabitat = habitatService.updateHabitat(id, habitatDetails);
        return new ResponseEntity<>(updatedHabitat, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabitat(@PathVariable Integer id) {
        habitatService.deleteHabitat(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
