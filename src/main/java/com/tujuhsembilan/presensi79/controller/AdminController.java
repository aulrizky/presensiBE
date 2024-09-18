package com.tujuhsembilan.presensi79.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.AdministratorRegistrationRequest;
import com.tujuhsembilan.presensi79.dto.request.EmployeeRegistrationRequest;
import com.tujuhsembilan.presensi79.exception.CustomErrorWithStatusException;
import com.tujuhsembilan.presensi79.service.EmployeeService;
import com.tujuhsembilan.presensi79.service.SuperAdminService;
import com.tujuhsembilan.presensi79.util.Messages;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Admin", description = Messages.TAG_SUPER_ADMIN_DESCRIPTION)
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {

    private final EmployeeService employeeService;
    private final SuperAdminService superAdminService;

    @PostMapping(path = "/create-employee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> createEmployee(@Valid @RequestBody EmployeeRegistrationRequest request) {
        MessageResponse response = employeeService.registerEmployee(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
