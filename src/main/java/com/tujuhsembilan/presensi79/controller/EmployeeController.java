package com.tujuhsembilan.presensi79.controller;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.EditProfileRequest;
import com.tujuhsembilan.presensi79.dto.request.LoginRequest;
import com.tujuhsembilan.presensi79.security.jwt.JwtUtils;
import com.tujuhsembilan.presensi79.service.EmployeeService;
import com.tujuhsembilan.presensi79.util.Messages;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Employee", description = Messages.TAG_EMPLOYEE_DESCRIPTION)
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(
        path = "/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> loginEmployee(@Valid @RequestBody LoginRequest request) {
        MessageResponse response = employeeService.loginEmployee(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping(
        path = "/profile",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> getEmployeeProfile(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idEmployee = jwtUtils.getEmployeeIdFromJwtToken(authToken);
        MessageResponse response = employeeService.getEmployeeProfile(idEmployee);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping(
        path = "/personal-info",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> getEmployeePersonalInformation(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idEmployee = jwtUtils.getEmployeeIdFromJwtToken(authToken);
        MessageResponse response = employeeService.getEmployeePersonalInformation(idEmployee);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping(
        path = "/professional-info",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> getEmployeeProfessionalInformation(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idEmployee = jwtUtils.getEmployeeIdFromJwtToken(authToken);
        MessageResponse response = employeeService.getEmployeeProfessionalInformation(idEmployee);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping(
        path = "/edit-profile",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> editEmployeeProfile(HttpServletRequest request, @Valid @RequestBody EditProfileRequest profileRequest) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idEmployee = jwtUtils.getEmployeeIdFromJwtToken(authToken);
        MessageResponse response = employeeService.editEmployeeProfile(idEmployee, profileRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping(
        path = "/change-profile-picture",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> changeProfilePicture(HttpServletRequest request, @RequestPart("file") MultipartFile file) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idEmployee = jwtUtils.getEmployeeIdFromJwtToken(authToken);
        MessageResponse response = employeeService.changeProfilePicture(idEmployee, file);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
