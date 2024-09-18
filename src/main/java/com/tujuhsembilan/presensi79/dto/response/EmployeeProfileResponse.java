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
public class EmployeeProfileResponse {

    @JsonProperty("id_employee")
    private Integer idEmployee;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("role_current_company")
    private String roleCurrentCompany;

    @JsonProperty("profile_picture")
    private String profilePicture;

    @JsonProperty("id_company")
    private Integer idCompany;
}
