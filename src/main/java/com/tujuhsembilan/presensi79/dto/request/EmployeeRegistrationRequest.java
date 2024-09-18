package com.tujuhsembilan.presensi79.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRegistrationRequest {

    @NotEmpty
    @JsonProperty("first_name")
    private String firstName;

    @NotEmpty
    @JsonProperty("last_name")
    private String lastName;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String email;

    @NotEmpty
    @JsonProperty("employee_number")
    private String employeeNumber;

    @NotNull
    @JsonProperty("id_department")
    private Integer idDepartment;

    @NotEmpty
    @JsonProperty("role_current_company")
    private String roleCurrentCompany;

    @NotNull
    @JsonProperty("id_company")
    private Integer idCompany;
}
