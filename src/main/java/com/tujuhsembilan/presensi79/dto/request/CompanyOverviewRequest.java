package com.tujuhsembilan.presensi79.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyOverviewRequest {

    // @JsonProperty("start_date_filter")
    private LocalDate start_date_filter;

    // @JsonProperty("end_date_filter")
    private LocalDate end_date_filter;
}