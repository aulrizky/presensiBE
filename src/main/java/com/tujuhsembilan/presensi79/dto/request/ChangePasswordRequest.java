package com.tujuhsembilan.presensi79.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {

    @NotEmpty(message = "Old password is required")
    @JsonProperty("old_password")
    private String oldPassword;

    @NotEmpty(message = "New password is required")
    @JsonProperty("new_password")
    private String newPassword;

    @NotEmpty(message = "Confirm password is required")
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
