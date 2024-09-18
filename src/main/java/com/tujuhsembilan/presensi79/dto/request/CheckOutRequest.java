package com.tujuhsembilan.presensi79.dto.request;

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
public class CheckOutRequest {

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("check_out_note")
    private String checkOutNote;

    @JsonProperty("check_out_time")
    private LocalDateTime checkOutTime;
}
