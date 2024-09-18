package com.tujuhsembilan.presensi79.controller;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.TermsConditionsRequest;
import com.tujuhsembilan.presensi79.security.jwt.JwtUtils;
import com.tujuhsembilan.presensi79.service.CompanyConfigService;
import com.tujuhsembilan.presensi79.util.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "CompanyConfig", description = Messages.TAG_COMPANY_CONFIG_DESCRIPTION)
@RestController
@RequestMapping("/company-config")
public class CompanyConfigController {

    @Autowired
    private CompanyConfigService companyConfigService;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "Get Company Configuration", description = Messages.OPERATION_GET_COMPANY_CONFIG)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Messages.SUCCESS_RETRIEVE_CONFIG,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = Messages.ERROR_NOT_FOUND_CONFIG,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "500", description = Messages.ERROR_INTERNAL,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> getCompanyConfig(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idCompany = jwtUtils.getCompanyIdFromJwtToken(authToken);
        MessageResponse response = companyConfigService.getCompanyConfig(idCompany);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Get Terms and Conditions", description = Messages.OPERATION_GET_TERMS_CONDITIONS)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Messages.SUCCESS_RETRIEVE_CONFIG,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = Messages.ERROR_NOT_FOUND_CONFIG,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "500", description = Messages.ERROR_INTERNAL,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/terms-and-conditions")
    public ResponseEntity<MessageResponse> getTermsConditions(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idCompany = jwtUtils.getCompanyIdFromJwtToken(authToken);
        MessageResponse response = companyConfigService.getTermsConditions(idCompany);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Update Terms and Conditions", description = Messages.OPERATION_UPDATE_TERMS_CONDITIONS)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Messages.SUCCESS_UPDATE_CONFIG,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = Messages.ERROR_INVALID_DATA,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "500", description = Messages.ERROR_INTERNAL,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PatchMapping("/terms-and-conditions")
    public ResponseEntity<MessageResponse> updateTermsConditions(
            HttpServletRequest request,
            @RequestBody TermsConditionsRequest termsConditionsRequest) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idCompany = jwtUtils.getCompanyIdFromJwtToken(authToken);
        MessageResponse response = companyConfigService.updateTermsConditions(idCompany, termsConditionsRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Create Terms and Conditions", description = Messages.OPERATION_CREATE_TERMS_CONDITIONS)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Messages.SUCCESS_CREATE_CONFIG,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = Messages.ERROR_INVALID_DATA,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "500", description = Messages.ERROR_INTERNAL,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/terms-and-conditions")
    public ResponseEntity<MessageResponse> createTermsConditions(
            HttpServletRequest request,
            @RequestBody TermsConditionsRequest termsConditionsRequest) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idCompany = jwtUtils.getCompanyIdFromJwtToken(authToken);
        MessageResponse response = companyConfigService.createTermsConditions(idCompany, termsConditionsRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
