package com.tujuhsembilan.presensi79.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyConfigResponse {

    @JsonProperty("id_company_config")
    private Integer idCompanyConfig;

    @JsonProperty("id_company")
    private Integer idCompany;

    @JsonProperty("working_day_flexible")
    private Boolean workingDayFlexible;

    @JsonProperty("working_day_start")
    private String workingDayStart;

    @JsonProperty("working_day_end")
    private String workingDayEnd;

    @JsonProperty("working_hours_flexible")
    private Boolean workingHoursFlexible;

    @JsonProperty("working_hours_start")
    private LocalTime workingHoursStart;

    @JsonProperty("working_hours_end")
    private LocalTime workingHoursEnd;

    @JsonProperty("working_duration_flexible")
    private Boolean workingDurationFlexible;

    @JsonProperty("working_duration")
    private Integer workingDuration;

    @JsonProperty("check_in_tolerance_flexible")
    private Boolean checkInToleranceFlexible;

    @JsonProperty("check_in_tolerance")
    private Integer checkInTolerance;

    @JsonProperty("check_out_tolerance_flexible")
    private Boolean checkOutToleranceFlexible;

    @JsonProperty("check_out_tolerance")
    private Integer checkOutTolerance;

    @JsonProperty("break_time_flexible")
    private Boolean breakTimeFlexible;

    @JsonProperty("break_time")
    private Integer breakTime;

    @JsonProperty("after_break_tolerance_flexible")
    private Boolean afterBreakToleranceFlexible;

    @JsonProperty("after_break_tolerance")
    private Integer afterBreakTolerance;

    @JsonProperty("geolocation")
    private Boolean geolocation;

    @JsonProperty("geolocation_radius")
    private Integer geolocationRadius;

    @JsonProperty("selfie_mode")
    private Boolean selfieMode;

    @JsonProperty("auto_check_out")
    private Boolean autoCheckOut;

    @JsonProperty("auto_check_out_time")
    private LocalTime autoCheckOutTime;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private Timestamp createdDate;

    @JsonProperty("modified_by")
    private String modifiedBy;

    @JsonProperty("modified_date")
    private Timestamp modifiedDate;

    @JsonProperty("is_deleted")
    private Boolean isDeleted;
}
