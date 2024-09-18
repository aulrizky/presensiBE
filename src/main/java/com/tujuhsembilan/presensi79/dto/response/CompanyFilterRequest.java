package com.tujuhsembilan.presensi79.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyFilterRequest {

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("start_date_joined")
    private String startDateJoined;

    @JsonProperty("end_date_joined")
    private String endDateJoined;

}
