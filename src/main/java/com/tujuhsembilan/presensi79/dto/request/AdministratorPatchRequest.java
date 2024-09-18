package com.tujuhsembilan.presensi79.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministratorPatchRequest {

    @JsonProperty("profile_picture")
    private String profilePicture;

    @JsonProperty("first_name")
    @Size(min = 1, max = 255, message = "Invalid first name. (Min. 1 characters & Max. 255 characters)")
    private String firstName;

    @JsonProperty("last_name")
    @Size(min = 1, max = 255, message = "Invalid last name. (Min. 1 characters & Max. 255 characters)")
    private String lastName;

    @JsonProperty("email")
    @Size(min = 1, max = 255, message = "Invalid email. (Min. 1 characters & Max 255 characters)")
    private String email;

    @JsonProperty("username")
    @Size(min = 7, max = 20, message = "Invalid username. (Min. 7 characters & Max. 20 characters)")
    private String username;

    @JsonProperty("id_company")
    private Integer idCompany;
}
