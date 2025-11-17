package com.example.animals.dto;

import com.example.animals.model.Animal;

/**
 * DTO para representar un Animal con información completa de su Keeper
 */
public class AnimalWithKeeperDTO {
    private Animal animal;
    private KeeperDTO keeper;

    public AnimalWithKeeperDTO() {}

    public AnimalWithKeeperDTO(Animal animal, KeeperDTO keeper) {
        this.animal = animal;
        this.keeper = keeper;
    }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public KeeperDTO getKeeper() { return keeper; }
    public void setKeeper(KeeperDTO keeper) { this.keeper = keeper; }
}
