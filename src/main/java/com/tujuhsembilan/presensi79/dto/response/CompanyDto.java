package com.tujuhsembilan.presensi79.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    @JsonProperty
    private Integer id_company;

    @JsonProperty
    private String company_name;

    @JsonProperty
    private String company_logo;

    @JsonProperty
    private String founder;

    @JsonProperty
    private LocalDate founded_at;

    @JsonProperty
    private String phone;

    @JsonProperty
    private String address;

    @JsonProperty
    private String state;

    @JsonProperty
    private String city;

    @JsonProperty
    private String zip_code;

    @JsonProperty
    private LocalDate joining_date;

    @JsonProperty
    private String email;
}
