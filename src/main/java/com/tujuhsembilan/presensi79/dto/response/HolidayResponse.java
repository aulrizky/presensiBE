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
public class HolidayResponse {

    @JsonProperty("id_holiday")
    private Integer idHoliday;

    @JsonProperty("holiday_name")
    private String holidayName;

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("is_deleted")
    private Boolean isDeleted;
    
}
