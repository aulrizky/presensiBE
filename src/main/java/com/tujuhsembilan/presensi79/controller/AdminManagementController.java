package com.tujuhsembilan.presensi79.controller;

import com.tujuhsembilan.presensi79.dto.request.AdminPhotoRequest;
import com.tujuhsembilan.presensi79.dto.request.CompanyPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.AdministratorPatchRequest;
import com.tujuhsembilan.presensi79.dto.request.AdminManagementRequest;
import com.tujuhsembilan.presensi79.dto.request.AdministratorRegistrationRequest;
import com.tujuhsembilan.presensi79.exception.CustomErrorWithStatusException;
import com.tujuhsembilan.presensi79.service.AdminManagementService;
import com.tujuhsembilan.presensi79.service.SuperAdminService;
import com.tujuhsembilan.presensi79.util.Messages;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Admin Management", description = Messages.TAG_ADMIN_DESCRIPTION)
@RestController
@RequestMapping("/admin-management")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminManagementController {

  private final SuperAdminService superAdminService;
  private final AdminManagementService adminManagementService;

  // --> get all list admin
  @GetMapping(path = "/admins", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MessageResponse> getDaftarAdmin(
          @RequestParam(defaultValue = "1") int pageNumber,
          @RequestParam(defaultValue = "10") int pageSize,
          @ModelAttribute AdminManagementRequest filter) {
    MessageResponse response = adminManagementService.getAdminList(
            pageNumber,
            pageSize,
            filter);
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @GetMapping("/admins/{idAdmin}")
  public ResponseEntity<MessageResponse> getDetailAdmin(@PathVariable("idAdmin") Integer idAdmin) {
    MessageResponse response = superAdminService.getDetailAdminById(idAdmin);
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @PostMapping(path = "/admins", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MessageResponse> createAdministrator(@RequestBody AdministratorRegistrationRequest request) {
    MessageResponse response;
    try {
      response = superAdminService.registerAdministratorTransactional(request);
    } catch (CustomErrorWithStatusException e) {
      response = new MessageResponse();
      response.setStatusCode(e.getStatus().value());
      response.setStatus(e.getStatus().getReasonPhrase());
      response.setErrorMessage(e.getErrorMessage());
      response.setMessage(e.getMessage());
    }
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @PatchMapping("/admins/photo/{id_admin}")
  public ResponseEntity<MessageResponse> patchAdminPhoto(@PathVariable Integer id_admin, @RequestParam("profile_picture") MultipartFile profilePicture) {
    MessageResponse response = adminManagementService.patchAdminPhto(id_admin, profilePicture);
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @PatchMapping(path = "/admins/{idAdmin}", consumes = { MediaType.APPLICATION_JSON_VALUE,
          MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<MessageResponse> patchAdministrator(@PathVariable("idAdmin") Integer idAdmin,
                                                            @RequestPart("data") AdministratorPatchRequest request,
                                                            @RequestPart(value = "profile_picture", required = false) MultipartFile profilePicture) {
    MessageResponse response;
    try {
      response = superAdminService.patchAdministratorTransactional(idAdmin, request, profilePicture);
    } catch (CustomErrorWithStatusException e) {
      response = new MessageResponse();
      response.setStatusCode(e.getStatus().value());
      response.setStatus(e.getStatus().getReasonPhrase());
      response.setErrorMessage(e.getErrorMessage());
      response.setMessage(e.getMessage());
    }
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @GetMapping("/master-company")
    public ResponseEntity<MessageResponse> getCompanies() {
        MessageResponse response = adminManagementService.getCompanyList();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}

