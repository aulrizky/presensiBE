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
public class AdminPhotoResponse {

    @JsonProperty
    private Integer id_admin;

    @JsonProperty
    private String profile_picture;
}
