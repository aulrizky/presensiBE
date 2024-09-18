package com.tujuhsembilan.presensi79.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeePersonalInformationResponse {

    @JsonProperty("id_employee")
    private Integer idEmployee;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("marital_status")
    private String maritalStatus;

    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("address")
    private String address;

    @JsonProperty("province")
    private String province;

    @JsonProperty("city")
    private String city;

    @JsonProperty("district")
    private String district;

    @JsonProperty("zip_code")
    private String zipCode;
}
