package com.tujuhsembilan.presensi79.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.CompanyOverviewRequest;
import com.tujuhsembilan.presensi79.dto.request.LoginRequest;
import com.tujuhsembilan.presensi79.dto.request.SuperAdminRegistrationRequest;
import com.tujuhsembilan.presensi79.service.SuperAdminService;
import com.tujuhsembilan.presensi79.util.Messages;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "SuperAdmin", description = Messages.TAG_ADMIN_DESCRIPTION)
@RestController
@RequestMapping("/superadmin")
public class SuperAdminController {
  
  @Autowired
  private SuperAdminService superAdminService;

  @PostMapping(
      path = "/register",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<MessageResponse> createEmployee(@Valid @RequestBody SuperAdminRegistrationRequest request) {
      MessageResponse response = superAdminService.registerSuperAdmin(request);
      return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @PostMapping(
    path = "/login",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
    public ResponseEntity<MessageResponse> loginSuperadmin(@Valid @RequestBody LoginRequest request) {
        MessageResponse response = superAdminService.loginSuperAdmin(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
  
  @GetMapping("/data-overview")
    public ResponseEntity<MessageResponse> getDataOverview() {
      MessageResponse response = superAdminService.dataOverviewSuperadmin();
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }

  @GetMapping("/company-overview")
    public ResponseEntity<MessageResponse> getCompanyOverview(
      @ModelAttribute CompanyOverviewRequest request
    ) {
      MessageResponse response = superAdminService.dataCompanyOverview(request);
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}


