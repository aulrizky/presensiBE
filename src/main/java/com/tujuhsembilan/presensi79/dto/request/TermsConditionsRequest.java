package com.tujuhsembilan.presensi79.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermsConditionsRequest {
    @JsonProperty("terms_conditions")
    private String terms_conditions;
}
