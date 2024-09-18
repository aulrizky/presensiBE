package com.tujuhsembilan.presensi79.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveCardResponse {

    @JsonProperty("id_leave")
    private Integer idLeave;

    @JsonProperty("id_employee")
    private Integer idEmployee;

    @JsonProperty("leave_title")
    private String leaveTitle;

    @JsonProperty("leave_type")
    private String leaveType;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("is_deleted")
    private Boolean isDeleted;
}
