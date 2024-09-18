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
public class SuperAdminRegistrationRequest {

    @NotEmpty
    @JsonProperty("name")
    private String name;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

}
