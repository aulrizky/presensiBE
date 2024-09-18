package com.tujuhsembilan.presensi79.controller;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.CheckInRequest;
import com.tujuhsembilan.presensi79.dto.request.CheckOutRequest;
import com.tujuhsembilan.presensi79.security.jwt.JwtUtils;
import com.tujuhsembilan.presensi79.service.AttendanceService;
import com.tujuhsembilan.presensi79.util.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Tag(name = "Attendance", description = Messages.TAG_ATTENDANCE_DESCRIPTION)
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AttendanceController(AttendanceService attendanceService, JwtUtils jwtUtils) {
        this.attendanceService = attendanceService;
        this.jwtUtils = jwtUtils;
    }
    
    @Operation(summary = "Get Attendance Details", description = Messages.OPERATION_GET_ATTENDANCE_DETAILS)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Messages.SUCCESS_RETRIEVE_ATTENDANCE_DETAILS,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = Messages.ERROR_NOT_FOUND_ATTENDANCE_DETAILS,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "500", description = Messages.ERROR_INTERNAL,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping(
            path = "/details",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> getAttendanceDetails(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idEmployee = jwtUtils.getEmployeeIdFromJwtToken(authToken);
        MessageResponse response = attendanceService.getAttendanceDetailsByEmployee(idEmployee);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Check-In Employee Attendance", description = Messages.OPERATION_CHECKIN_ATTENDANCE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = Messages.SUCCESS_CREATE_ATTENDANCE,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = Messages.ERROR_INVALID_DATA,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "500", description = Messages.ERROR_INTERNAL,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping(
        path = "/check-in",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> checkInEmployeeAttendance(
        HttpServletRequest request,
        @Valid @RequestBody CheckInRequest checkInRequest) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idEmployee = jwtUtils.getEmployeeIdFromJwtToken(authToken);
        MessageResponse response = attendanceService.checkInAttendance(idEmployee, checkInRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Check-Out Employee Attendance", description = Messages.OPERATION_CHECKOUT_ATTENDANCE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Messages.SUCCESS_CHECKOUT,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = Messages.ERROR_INVALID_DATA,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "500", description = Messages.ERROR_INTERNAL,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PatchMapping(
        path = "/check-out",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> checkOutEmployeeAttendance(
        HttpServletRequest request,
        @Valid @RequestBody CheckOutRequest checkOutRequest) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idEmployee = jwtUtils.getEmployeeIdFromJwtToken(authToken);
        MessageResponse response = attendanceService.checkOutAttendance(idEmployee, checkOutRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Get Days Worked", description = Messages.OPERATION_GET_DAYS_WORKED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Messages.SUCCESS_RETRIEVE_DAYS_WORKED,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = Messages.ERROR_NOT_FOUND_ATTENDANCE_RECORDS,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "500", description = Messages.ERROR_INTERNAL,
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping(
            path = "/days-worked",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> getDaysWorked(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idEmployee = jwtUtils.getEmployeeIdFromJwtToken(authToken);
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Jakarta"));
        MessageResponse response = attendanceService.getDaysWorkedByEmployee(idEmployee, currentDate);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
