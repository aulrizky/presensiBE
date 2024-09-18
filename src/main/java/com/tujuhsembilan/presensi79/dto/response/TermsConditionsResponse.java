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
public class TermsConditionsResponse {

    @JsonProperty("id_company")
    private Integer idCompany;

    @JsonProperty("terms_conditions")
    private String terms_conditions;
}
