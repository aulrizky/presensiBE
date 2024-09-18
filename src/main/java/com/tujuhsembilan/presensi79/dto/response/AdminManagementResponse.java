package com.tujuhsembilan.presensi79.dto.response;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminManagementResponse {
  @JsonProperty("id_admin")
  private Integer idAdmin;

  @JsonProperty("company")
  private CompanyResponse company;

  @JsonProperty("profile_picture")
  private String profilePicture;

  @JsonProperty("first_name")
  private String firstName;
  
  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("email")
  private String email;

  @JsonProperty("created_day")
  private Timestamp createdDate;
}
