package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String surname;
    private String otherNames;
    private LocalDate dateOfBirth;
    
    @Column(columnDefinition = "TEXT")
    private String idPhoto;  // Base64 encoded string
    private String employeeNumber;  // Unique identifier
    private String validationCode;  // 10-digit code
    private boolean isValidationCodeUsed = false;
}
