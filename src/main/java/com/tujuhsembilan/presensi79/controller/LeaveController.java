package com.tujuhsembilan.presensi79.controller;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.LeaveRequest;
import com.tujuhsembilan.presensi79.service.LeaveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.tujuhsembilan.presensi79.security.jwt.JwtUtils;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/employee")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(LeaveController.class);

    @GetMapping(
        path = "/all-leaves",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> getAllLeaves(HttpServletRequest request,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        try {
            String authToken = request.getHeader("Authorization").substring(7);
            Integer idEmployee = jwtUtils.getEmployeeIdFromJwtToken(authToken);
            logger.info("Fetching all leaves for employee ID: {}", idEmployee);
            MessageResponse response = leaveService.getAllLeaves(idEmployee, page, size);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            logger.error("Error fetching all leaves: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new MessageResponse("Error fetching all leaves", HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR", e.getMessage())
            );
        }
    }

    @PostMapping(
        path = "/apply-leave",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> applyLeave(
        HttpServletRequest request,
        @RequestParam("leaveTitle") String leaveTitle,
        @RequestParam("leaveType") String leaveType,
        @RequestParam("startDate") @Valid String startDate,
        @RequestParam("endDate") @Valid String endDate,
        @RequestParam("reason") String reason,
        @RequestPart(value = "attachment", required = false) MultipartFile attachment) {
        try {
            String authToken = request.getHeader("Authorization").substring(7);
            Integer idEmployee = jwtUtils.getEmployeeIdFromJwtToken(authToken);
            logger.info("Applying leave for employee ID: {}", idEmployee);

            // Membuat LeaveRequest dari parameter
            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setLeaveTitle(leaveTitle);
            leaveRequest.setLeaveType(leaveType);
            leaveRequest.setStartDate(LocalDate.parse(startDate));
            leaveRequest.setEndDate(LocalDate.parse(endDate));
            leaveRequest.setReason(reason);

            MessageResponse response = leaveService.applyLeave(idEmployee, leaveRequest, attachment);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            logger.error("Error applying leave: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new MessageResponse("Error applying leave", HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR", e.getMessage())
            );
        }
    }
}
