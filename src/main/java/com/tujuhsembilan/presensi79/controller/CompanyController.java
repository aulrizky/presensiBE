package com.tujuhsembilan.presensi79.controller;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.security.jwt.JwtUtils;
import com.tujuhsembilan.presensi79.service.CompanyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import com.tujuhsembilan.presensi79.util.Messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Company", description = Messages.TAG_COMPANY_DESCRIPTION)
@RestController
@RequestMapping("/company-profile")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping(
        path = "/logo",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> getCompanyLogo(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idCompany = jwtUtils.getCompanyIdFromJwtToken(authToken);
        MessageResponse response = companyService.getCompanyLogo(idCompany);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping(
        path = "/coordinates",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> getCompanyCoordinates(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idCompany = jwtUtils.getCompanyIdFromJwtToken(authToken);
        MessageResponse response = companyService.getCompanyCoordinates(idCompany);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
