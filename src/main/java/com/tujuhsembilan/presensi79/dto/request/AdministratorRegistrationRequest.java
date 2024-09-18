package com.tujuhsembilan.presensi79.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministratorRegistrationRequest {

    @JsonProperty("first_name")
    @NotEmpty(message = "first name must not be empty")
    @Size(min = 1, max = 255, message = "Invalid first name. (Min. 1 characters & Max. 255 characters)")
    private String firstName;

    @JsonProperty("last_name")
    @NotEmpty(message = "last name must not be empty")
    @Size(min = 1, max = 255, message = "Invalid last name. (Min. 1 characters & Max. 255 characters)")
    private String lastName;

    @JsonProperty("username")
    @NotEmpty(message = "username must not be empty")
    @Size(min = 7, max = 20, message = "Invalid username. (Min. 7 characters & Max. 20 characters)")
    private String username;

    @JsonProperty("email")
    @NotEmpty(message = "email must not be empty")
    @Size(min = 1, max = 255, message = "Invalid email. (Min. 1 characters & Max 255 characters)")
    private String email;

    @JsonProperty("password")
    @NotEmpty(message = "password must not be empty")
    @Size(min = 7, max = 20, message = "Invalid password. (Min. 7 characters & Max 20 characters)")
    private String password;

    @JsonProperty("id_company")
    @NotNull(message = "id company must not be empty")
    private Integer idCompany;

}
