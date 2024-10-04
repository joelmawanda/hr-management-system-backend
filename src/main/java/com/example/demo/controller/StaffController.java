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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/api/staff")
@ApiResponses(value = {

        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description="Successful operation"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description="Bad Request. The request was invalid or cannot be served"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description="Unauthorized. The authentication failed"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description="Forbidden. The user may not be having the necessary permissions for a resource"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description="Not Found. The resource could not be found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description="Internal server error. The server encountered an unexpected condition which prevented it fulfilling the request"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "timestamp", description="The time at which the api was invoked"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "operation_result", description="0 means Success while 1 means Failed"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "operation_description", description="Success describes operation_result of 0 whereas Failed describes operation_result of 1"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "message", description="The response message for the api operation")

})
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Generate validation code
    @Operation(summary = "Generate staff validation code", description = "Generates a validation code required in staff registration")
    @GetMapping("/generateValidationCode")
    public ResponseEntity<?> generateStaffValidationCode() {
        String validationCode = staffService.generateValidationCode();
        Staff newStaff = new Staff();
        newStaff.setValidationCode(validationCode);
        staffService.saveStaff(newStaff);
//        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "Your validation code is : " + newStaff.getValidationCode()), HttpStatus.OK);
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "Successfully generated code", newStaff.getValidationCode()), HttpStatus.OK);
    }

    //Staff registration API
    @Operation(summary = "Save Staff details", description = "Saves staff details to the database")
    @PutMapping("/register")
    public ResponseEntity<?> saveStaffDetails(@RequestParam String validationCode, @Valid @RequestBody StaffDTO staffDTO) {
        Staff staffDetails = staffService.saveStaffDetails(validationCode, staffDTO);
//        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "Staff created successfully with employee number: " + staffDetails.getEmployeeNumber()), HttpStatus.OK);
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "Staff created successfully", staffDetails.getEmployeeNumber()), HttpStatus.OK);
    }

    //Staff Retrieval API
    @Operation(summary = "Retrieve staff details", description = "Retrieves staff details from the database")
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
    @Operation(summary = "Update staff details", description = "For Updating staff details ")
    @PutMapping("/update")
    public ResponseEntity<?> updateStaff(@RequestParam String employeeNumber, @RequestBody StaffUpdateDTO staffUpdateDTO) {
        Staff updatedStaff = staffService.updateStaff(employeeNumber, staffUpdateDTO);
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "Staff details updated successfully"), HttpStatus.OK);
    }

    //Create User API
    @Operation(summary = "Save User details", description = "Saves user details to the database")
    @PostMapping("/register/user")
    public ResponseEntity<?> saveUser(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        UserInfo userInfo = staffService.saveUser(authRequestDTO);
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "User details saved successfully"), HttpStatus.OK);
    }

    @Operation(summary = "Authenticate user", description = "Authenticates a user and generates a JWT token for the user")
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateAndGetToken(@Valid @RequestBody AuthRequestDTO authRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        String token = jwtService.generateToken(authRequestDTO.getUsername());
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, "Authenticated Successfully", token), HttpStatus.OK);
    }
}

