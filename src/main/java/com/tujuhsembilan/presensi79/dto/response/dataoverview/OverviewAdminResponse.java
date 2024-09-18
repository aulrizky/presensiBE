package com.tujuhsembilan.presensi79.dto.response.dataoverview;

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
public class OverviewAdminResponse {
    @JsonProperty("total_admin")
    private Integer totalAdmin;

    @JsonProperty("last_update")
    private Timestamp lastUpdate;
}
