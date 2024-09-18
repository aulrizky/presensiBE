package com.tujuhsembilan.presensi79.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponse {

    @JsonProperty("id_attendance")
    private Integer idAttendance;

    @JsonProperty("id_employee")
    private Integer idEmployee;

    @JsonProperty("date_attendance")
    private LocalDate dateAttendance;

    @JsonProperty("check_in")
    private LocalDateTime checkIn;

    @JsonProperty("check_out")
    private LocalDateTime checkOut;

    @JsonProperty("total_working_hours")
    private LocalTime totalWorkingHours;

    @JsonProperty("status")
    private String status;

    @JsonProperty("check_in_proof")
    private String checkInProof;

    @JsonProperty("check_out_proof")
    private String checkOutProof;

    @JsonProperty("check_out_note")
    private String checkOutNote;
    
    @JsonProperty("is_deleted")
    private Boolean isDeleted;
}
