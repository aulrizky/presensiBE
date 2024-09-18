package com.tujuhsembilan.presensi79.dto.response.superadmin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuperAdminRegistrationResponse {

    @JsonProperty("id_account")
    private String idAccount;

    @JsonProperty("id_superadmin")
    private Integer idSuperAdmin;

    @JsonProperty("name")
    private String name;


    @JsonProperty("username")
    private String username;


    @JsonProperty("role")
    private String role;

}