package com.example.animals.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @Column(unique = true)
    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotNull(message = "Weight cannot be null")
    @DecimalMin(value = "0.1", message = "Weight must be greater than 0")
    private Double weight;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Birth date time cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDateTime;

    @NotNull(message = "isWild cannot be null")
    private Boolean isWild;

    @ManyToOne
    @JoinColumn(name = "habitat_id", referencedColumnName = "id")
    @NotNull(message = "Habitat cannot be null")
    private Habitat habitat;

    // Constructors
    public Animal() {}

    public Animal(Long id, Double weight, String name, LocalDateTime birthDateTime, Boolean isWild, Habitat habitat) {
        this.id = id;
        this.weight = weight;
        this.name = name;
        this.birthDateTime = birthDateTime;
        this.isWild = isWild;
        this.habitat = habitat;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getBirthDateTime() { return birthDateTime; }
    public void setBirthDateTime(LocalDateTime birthDateTime) { this.birthDateTime = birthDateTime; }

    public Boolean getIsWild() { return isWild; }
    public void setIsWild(Boolean isWild) { this.isWild = isWild; }

    public Habitat getHabitat() { return habitat; }
    public void setHabitat(Habitat habitat) { this.habitat = habitat; }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", weight=" + weight +
                ", name='" + name + '\'' +
                ", birthDateTime=" + birthDateTime +
                ", isWild=" + isWild +
                ", habitat=" + (habitat != null ? habitat.getId() : null) +
                '}';
    }
}
