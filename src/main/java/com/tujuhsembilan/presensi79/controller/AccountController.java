package com.tujuhsembilan.presensi79.controller;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.ChangePasswordRequest;
import com.tujuhsembilan.presensi79.security.jwt.JwtUtils;
import com.tujuhsembilan.presensi79.service.AccountService;
import com.tujuhsembilan.presensi79.util.Messages;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account", description = Messages.TAG_ACCOUNT_DESCRIPTION)
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtils jwtUtils;

    @PatchMapping(
        path = "/change-password",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> changeEmployeePassword(
        HttpServletRequest request,
        @Valid @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idEmployee = jwtUtils.getEmployeeIdFromJwtToken(authToken);
        MessageResponse response = accountService.changeEmployeePassword(idEmployee, changePasswordRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
