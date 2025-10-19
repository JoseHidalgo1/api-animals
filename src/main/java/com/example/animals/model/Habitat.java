package com.example.animals.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "habitats")
public class Habitat {

    @Id
    @NotNull(message = "Id cannot be null")
    private Integer id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Area cannot be null")
    @DecimalMin(value = "0.1", message = "Area must be greater than 0")
    private Double area;

    @NotNull(message = "Established date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime establishedDate;

    @NotNull(message = "isCovered cannot be null")
    private Boolean isCovered;

    // Constructores
    public Habitat() {}

    public Habitat(Integer id, String name, Double area, LocalDateTime establishedDate, Boolean isCovered) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.establishedDate = establishedDate;
        this.isCovered = isCovered;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }

    public LocalDateTime getEstablishedDate() { return establishedDate; }
    public void setEstablishedDate(LocalDateTime establishedDate) { this.establishedDate = establishedDate; }

    public Boolean getIsCovered() { return isCovered; }
    public void setIsCovered(Boolean isCovered) { this.isCovered = isCovered; }

    @Override
    public String toString() {
        return "Habitat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", area=" + area +
                ", establishedDate=" + establishedDate +
                ", isCovered=" + isCovered +
                '}';
    }
}
