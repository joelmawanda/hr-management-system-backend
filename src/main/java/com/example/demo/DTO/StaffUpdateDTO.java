package com.example.demo.DTO;

import java.time.LocalDate;

//This DTO will be used for the Staff Update API to allow updates to Date of Birth and ID Photo only.
public class StaffUpdateDTO {
    private LocalDate dateOfBirth;
    private String idPhoto;  // Base64-encoded image string

    // Getters and Setters
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }
}

