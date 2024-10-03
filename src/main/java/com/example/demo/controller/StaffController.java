package com.example.demo.controller;

import com.example.demo.DTO.StaffDTO;
import com.example.demo.DTO.StaffUpdateDTO;
import com.example.demo.entity.Staff;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    // Generate validation code
    @GetMapping("/generateValidationCode")
    public ResponseEntity<String> generateStaffValidationCode() {

        String validationCode = staffService.generateValidationCode();

        Staff newStaff = new Staff();
        newStaff.setValidationCode(validationCode);

        staffService.saveStaff(newStaff);

        return ResponseEntity.ok("Your validation code is : " + newStaff.getValidationCode() + " please continue to registration");
    }

    //Staff registration API
    @PutMapping("/register")
    public ResponseEntity<?> saveStaffDetails(@RequestParam String validationCode, @Valid @RequestBody StaffDTO staffDTO) {
        Staff staffDetails = staffService.saveStaffDetails(validationCode, staffDTO);
        return ResponseEntity.ok("Staff created successfully with employee Number " + staffDetails.getEmployeeNumber());
    }

    //Staff Retrieval API
    @GetMapping("/retrieve")
    public ResponseEntity<?> retrieveStaff(@RequestParam(required = false) String employeeNumber) {
        if (employeeNumber != null) {
            Optional<Staff> staff = staffService.retrieveStaffByEmployeeNumber(employeeNumber);
            if (staff.isPresent()) {
                return ResponseEntity.ok(staff.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Staff not found");
            }
        } else {
            return ResponseEntity.ok(staffService.retrieveAllStaff());
        }
    }

    //Staff Update API
    @PutMapping("/update")
    public ResponseEntity<?> updateStaff(@RequestParam String employeeNumber, @RequestBody StaffUpdateDTO staffUpdateDTO) {
        Staff updatedStaff = staffService.updateStaff(employeeNumber, staffUpdateDTO);
        return ResponseEntity.ok("Staff details updated successfully");
    }
}

