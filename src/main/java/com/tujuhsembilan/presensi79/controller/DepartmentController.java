package com.tujuhsembilan.presensi79.controller;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.security.jwt.JwtUtils;
import com.tujuhsembilan.presensi79.service.DepartmentService;
import com.tujuhsembilan.presensi79.util.Messages;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Department", description = Messages.TAG_DEPARTMENT_DESCRIPTION)
@RestController
@RequestMapping("/company")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping(
        path = "/department",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> getDepartmentsByCompany(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idCompany = jwtUtils.getCompanyIdFromJwtToken(authToken);
        MessageResponse response = departmentService.getDepartmentsByCompany(idCompany);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
