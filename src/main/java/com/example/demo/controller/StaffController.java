package com.example.demo.controller;

import com.example.demo.DTO.AuthRequestDTO;
import com.example.demo.DTO.StaffDTO;
import com.example.demo.DTO.StaffUpdateDTO;
import com.example.demo.entity.Staff;
import com.example.demo.entity.UserInfo;
import com.example.demo.response.ApiResponse;
import com.example.demo.response.Constants;
import com.example.demo.service.JwtService;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Generate validation code
    @GetMapping("/generateValidationCode")
    public ResponseEntity<?> generateStaffValidationCode() {
        String validationCode = staffService.generateValidationCode();
        Staff newStaff = new Staff();
        newStaff.setValidationCode(validationCode);
        staffService.saveStaff(newStaff);
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "Your validation code is : " + newStaff.getValidationCode()), HttpStatus.OK);
    }

    //Staff registration API
    @PutMapping("/register")
    public ResponseEntity<?> saveStaffDetails(@RequestParam String validationCode, @Valid @RequestBody StaffDTO staffDTO) {
        Staff staffDetails = staffService.saveStaffDetails(validationCode, staffDTO);
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "Staff created successfully with employee number: " + staffDetails.getEmployeeNumber()), HttpStatus.OK);
    }

    //Staff Retrieval API
    @GetMapping("/retrieve")
    public ResponseEntity<?> retrieveStaff(@RequestParam(required = false) String employeeNumber) {
        if (employeeNumber != null) {
            Optional<Staff> staff = staffService.retrieveStaffByEmployeeNumber(employeeNumber);
            if (staff.isPresent()) {
                return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "Staff details", staff.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_FAILURE_CODE, Constants.OPERATION_FAILED_DESCRIPTION, "Staff not found"), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "Staff details", staffService.retrieveAllStaff()), HttpStatus.OK);
        }
    }

    //Staff Update API
    @PutMapping("/update")
    public ResponseEntity<?> updateStaff(@RequestParam String employeeNumber, @RequestBody StaffUpdateDTO staffUpdateDTO) {
        Staff updatedStaff = staffService.updateStaff(employeeNumber, staffUpdateDTO);
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "Staff details updated successfully"), HttpStatus.OK);
    }

    //Create User API
    @PostMapping("/register/user")
    public ResponseEntity<?> saveUser(@RequestBody AuthRequestDTO authRequestDTO) {
        UserInfo userInfo = staffService.saveUser(authRequestDTO);
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "User details saved successfully"), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        String token = jwtService.generateToken(authRequestDTO.getUsername());
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "Authenticated Successfully", token), HttpStatus.OK);
    }
}

