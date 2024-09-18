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
public class EmployeeRegistrationResponse {

    @JsonProperty("id_employee")
    private Integer idEmployee;

    @JsonProperty("id_account")
    private String idAccount;

    @JsonProperty("id_company")
    private Integer idCompany;

    @JsonProperty("id_department")
    private Integer idDepartment;

    @JsonProperty("employee_number")
    private String employeeNumber;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private String role;

    @JsonProperty("role_current_company")
    private String roleCurrentCompany;
}
