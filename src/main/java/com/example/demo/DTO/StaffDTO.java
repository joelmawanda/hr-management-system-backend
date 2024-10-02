package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;


//This DTO will be used for both Staff Registration and Staff Update operations.
public class StaffDTO {
    private String surname;
    private String otherNames;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private String idPhoto;  // Base64-encoded image string
    private String validationCode;

    // Getters and Setters
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

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

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }
}

