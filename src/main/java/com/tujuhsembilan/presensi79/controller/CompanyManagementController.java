package com.tujuhsembilan.presensi79.controller;

import com.tujuhsembilan.presensi79.dto.request.AddCompanyRequest;
import com.tujuhsembilan.presensi79.dto.request.CompanyPhoto;
import com.tujuhsembilan.presensi79.dto.response.CompanyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.response.CompanyFilterRequest;
import com.tujuhsembilan.presensi79.service.CompanyService;
import com.tujuhsembilan.presensi79.util.Messages;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Company Management", description = Messages.TAG_COMPANY_DESCRIPTION)
@RestController
@RequestMapping("/company-management")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyManagementController {

    private final CompanyService companyService;

    @GetMapping("/companies")
    public ResponseEntity<MessageResponse> getCompanies(
            @PageableDefault(page = 0, size = 8, sort = "companyName", direction = Direction.ASC) Pageable page,
            @ModelAttribute CompanyFilterRequest request) {

        MessageResponse response = companyService.getCompanies(page, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @GetMapping("/companies/{idCompany}")
    public ResponseEntity<MessageResponse> getCompaniesById(@PathVariable Integer idCompany) {

        MessageResponse response = companyService.getCompaniDto(idCompany);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/companies")
    public ResponseEntity<MessageResponse> addCompany(@RequestBody AddCompanyRequest addCompanyRequest) {
        MessageResponse response = companyService.postCompany(addCompanyRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping("/companies/logo/{idCompany}")
    public ResponseEntity<MessageResponse> patchCompanyPhoto(@PathVariable Integer idCompany, @RequestParam("company_logo") MultipartFile companyLogo) {
        MessageResponse response = companyService.patchCompanyPhoto(idCompany, companyLogo);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/companies/delete/{idCompany}")
    public ResponseEntity<MessageResponse> deleteCompany(@PathVariable Integer idCompany) {
        MessageResponse response = companyService.deleteCompany(idCompany);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
