package com.example.animals.dto;

import java.time.LocalDate;

/**
 * DTO para representar información de un Keeper obtenida del microservicio de Keepers
 */
public class KeeperDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate hireDate;
    private String specialization;
    private Boolean isActive;
    private Integer yearsOfExperience;

    // Constructores
    public KeeperDTO() {}

    public KeeperDTO(Long id, String firstName, String lastName, String email, 
                     LocalDate hireDate, String specialization, Boolean isActive, Integer yearsOfExperience) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hireDate = hireDate;
        this.specialization = specialization;
        this.isActive = isActive;
        this.yearsOfExperience = yearsOfExperience;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
