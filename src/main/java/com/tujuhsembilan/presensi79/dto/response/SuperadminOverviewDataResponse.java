package com.tujuhsembilan.presensi79.dto.response;

import com.tujuhsembilan.presensi79.dto.response.dataoverview.OverviewAdminResponse;
import com.tujuhsembilan.presensi79.dto.response.dataoverview.OverviewCompanyResponse;
import com.tujuhsembilan.presensi79.dto.response.dataoverview.OverviewEmployeeResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuperadminOverviewDataResponse {

    private OverviewCompanyResponse company;
    
    private OverviewAdminResponse admin;
    
    private OverviewEmployeeResponse employee;
}
