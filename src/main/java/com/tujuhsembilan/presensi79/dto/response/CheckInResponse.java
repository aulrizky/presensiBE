package com.tujuhsembilan.presensi79.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInResponse {

    @JsonProperty("id_attendance")
    private Integer idAttendance;

    @JsonProperty("id_employee")
    private Integer idEmployee;

    @JsonProperty("date_attendance")
    private LocalDate dateAttendance;

    @JsonProperty("check_in")
    private LocalDateTime checkIn;

    @JsonProperty("status")
    private String status;
}
