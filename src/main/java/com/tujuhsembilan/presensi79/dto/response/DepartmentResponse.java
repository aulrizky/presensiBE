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
public class DepartmentResponse {

    @JsonProperty("id_department")
    private Integer idDepartment;

    @JsonProperty("department_name")
    private String departmentName;

    @JsonProperty("is_deleted")
    private Boolean isDeleted;
}
