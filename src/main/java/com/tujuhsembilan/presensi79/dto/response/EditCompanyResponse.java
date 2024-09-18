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
public class EditCompanyResponse {
  
    @JsonProperty("id_company")
    private int idCompany;
  
    @JsonProperty("company_name")
    private String companyName;
  
    @JsonProperty("founder")
    private String founder;
  
    @JsonProperty("founded_at")
    private LocalDate foundedAt;
  
    @JsonProperty("email")
    private String email;
  
    @JsonProperty("phone")
    private String phone;
  
    @JsonProperty("address")
    private String address;
  
    @JsonProperty("state")
    private String state;
  
    @JsonProperty("city")
    private String city;
  
    @JsonProperty("zip_code")
    private String zipCode;
  
    @JsonProperty("joining_date")
    private LocalDate joiningDate;
}
