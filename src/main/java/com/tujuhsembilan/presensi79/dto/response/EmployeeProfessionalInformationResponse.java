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
public class EmployeeProfessionalInformationResponse {

    @JsonProperty("id_employee")
    private Integer idEmployee;
    
    @JsonProperty("employee_number")
    private String employeeNumber;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("id_department")
    private Integer idDepartment;

    @JsonProperty("department_name")
    private String departmentName;

    @JsonProperty("status")
    private String status;

    @JsonProperty("role")
    private String role;

    @JsonProperty("role_current_company")
    private String roleCurrentCompany;

    @JsonProperty("joining_date")
    private LocalDate joiningDate;
}
