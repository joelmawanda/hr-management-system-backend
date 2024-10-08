package com.example.demo.service;

import com.example.demo.DTO.AuthRequestDTO;
import com.example.demo.DTO.StaffDTO;
import com.example.demo.DTO.StaffUpdateDTO;
import com.example.demo.entity.Staff;
import com.example.demo.entity.UserInfo;
import com.example.demo.exception.InvalidVerificationCodeException;
import com.example.demo.exception.NoStaffFoundException;
import com.example.demo.repository.StaffRepository;
import com.example.demo.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StaffService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    // Update the staff member's details
    public Staff updateStaff(String employeeNumber, StaffUpdateDTO staffUpdateDTO) {
        Optional<Staff> staff = staffRepository.findByEmployeeNumber(employeeNumber);
        if (!staff.isPresent()) {
            throw new NoStaffFoundException("Staff not found");
        }

        Staff existingStaff = staff.get();
        existingStaff.setDateOfBirth(staffUpdateDTO.getDateOfBirth());
        existingStaff.setIdPhoto(staffUpdateDTO.getIdPhoto());

        return staffRepository.save(existingStaff);
    }

    // Create the staff member's details
    public Staff saveStaffDetails(String validationCode, StaffDTO staffDTO) {
        Optional<Staff> staffOptional = staffRepository.findByValidationCode(validationCode);

        if (!staffOptional.isPresent()) {
            throw new InvalidVerificationCodeException("Invalid Verification Code");
        }

        Staff existingStaff = staffOptional.get();

        if (existingStaff.isValidationCodeUsed()) {
            throw new InvalidVerificationCodeException("This validation code has already been used.");
        }

        existingStaff.setSurname(staffDTO.getSurname());
        existingStaff.setOtherNames(staffDTO.getOtherNames());
        existingStaff.setDateOfBirth(staffDTO.getDateOfBirth());
        existingStaff.setIdPhoto(staffDTO.getIdPhoto());
        existingStaff.setEmployeeNumber(generateEmployeeNumber());
        existingStaff.setValidationCodeUsed(true);

        return staffRepository.save(existingStaff);
    }

    // Method to get all staff
    public List<Staff> retrieveAllStaff() {
        return staffRepository.findAll();
    }

    // Method to retrieve a staff member by employee number
    public Optional<Staff> retrieveStaffByEmployeeNumber(String employeeNumber) {
        return staffRepository.findByEmployeeNumber(employeeNumber);
    }

    public Optional<Staff> findByValidationCode(String validationCode) {
        return staffRepository.findByValidationCode(validationCode);
    }

    public Staff saveStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    public String generateValidationCode() {
        // Generate a random 10-digit number as a string
        return String.format("%010d", (long) (Math.random() * 1_000_000_0000L));
    }

    public String generateEmployeeNumber() {
        // Generate a random 10-character employee number
        return UUID.randomUUID().toString().substring(0, 10);
    }

    // Create the user member's details
    public UserInfo saveUser(AuthRequestDTO authRequestDTO) {
        Optional<UserInfo> userOptional = userInfoRepository.findByName(authRequestDTO.getUsername());

        if (userOptional.isPresent()) {
            throw new InvalidVerificationCodeException("User already exists");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setName(authRequestDTO.getUsername());
        userInfo.setPassword(passwordEncoder.encode(authRequestDTO.getPassword()));
        userInfo.setRoles("ADMIN");

        return userInfoRepository.save(userInfo);
    }

}

