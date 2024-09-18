package com.tujuhsembilan.presensi79.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.security.jwt.JwtUtils;
import com.tujuhsembilan.presensi79.service.HolidayService;
import com.tujuhsembilan.presensi79.util.Messages;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Year;

@Tag(name = "Holiday", description = Messages.TAG_HOLIDAY_DESCRIPTION)
@RestController
@RequestMapping("/company")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/holidays")
    public ResponseEntity<MessageResponse> getAllHolidays(
            HttpServletRequest request, 
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        String authToken = request.getHeader("Authorization").substring(7);
        Integer idCompany = jwtUtils.getCompanyIdFromJwtToken(authToken);
        
        if (year == null) {
            year = Year.now().getValue();
        }

        MessageResponse response = holidayService.getHolidays(year, idCompany, page, size);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
