package com.tujuhsembilan.presensi79.dto.request;

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
public class EditProfileRequest {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("marital_status")
    private String maritalStatus;

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

    @JsonProperty("employee_number")
    private String employeeNumber;

    @JsonProperty("username")
    private String username;

    @JsonProperty("status")
    private String status;

    @JsonProperty("email")
    private String email;

    @JsonProperty("id_department")
    private Integer idDepartment;

    @JsonProperty("role_in_current_company")
    private String roleInCurrentCompany;

    @JsonProperty("role_in_client")
    private String roleInClient;

    @JsonProperty("joining_date")
    private LocalDate joiningDate;
}
