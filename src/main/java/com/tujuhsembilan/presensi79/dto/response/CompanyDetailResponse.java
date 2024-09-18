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
public class CompanyDetailResponse {

    @JsonProperty("id_company")
    private Integer idCompany;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("total_admin")
    private Long totalAdmin;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("created_date")
    private String createdDate;

}
