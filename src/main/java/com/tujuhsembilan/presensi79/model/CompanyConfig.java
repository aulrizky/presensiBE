package com.tujuhsembilan.presensi79.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalTime;

@Entity
@Table(name = "CompanyConfig")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_company_config", nullable = false, unique = true, updatable = false)
    private Integer idCompanyConfig;

    @OneToOne
    @JoinColumn(name = "id_company", referencedColumnName = "id_company")
    private Company company;

    @Column(name = "working_day_flexible", nullable = true)
    private Boolean workingDayFlexible;

    @Column(name = "working_day_start", nullable = true)
    private String workingDayStart;

    @Column(name = "working_day_end", nullable = true)
    private String workingDayEnd;

    @Column(name = "working_hours_flexible", nullable = true)
    private Boolean workingHoursFlexible;

    @Column(name = "working_hours_start", nullable = true)
    private LocalTime workingHoursStart;

    @Column(name = "working_hours_end", nullable = true)
    private LocalTime workingHoursEnd;

    @Column(name = "working_duration_flexible", nullable = true)
    private Boolean workingDurationFlexible;

    @Column(name = "working_duration", nullable = true)
    private Integer workingDuration;

    @Column(name = "check_in_tolerance_flexible", nullable = true)
    private Boolean checkInToleranceFlexible;

    @Column(name = "check_in_tolerance", nullable = true)
    private Integer checkInTolerance;

    @Column(name = "check_out_tolerance_flexible", nullable = true)
    private Boolean checkOutToleranceFlexible;

    @Column(name = "check_out_tolerance", nullable = true)
    private Integer checkOutTolerance;

    @Column(name = "break_time_flexible", nullable = true)
    private Boolean breakTimeFlexible;

    @Column(name = "break_time", nullable = true)
    private Integer breakTime;

    @Column(name = "after_break_tolerance_flexible", nullable = true)
    private Boolean afterBreakToleranceFlexible;

    @Column(name = "after_break_tolerance", nullable = true)
    private Integer afterBreakTolerance;

    @Column(name = "geolocation", nullable = true)
    private Boolean geolocation;

    @Column(name = "geolocation_radius", nullable = true)
    private Integer geolocationRadius;

    @Column(name = "selfie_mode", nullable = true)
    private Boolean selfieMode;

    @Column(name = "default_holiday", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean defaultHoliday;

    @Column(name = "terms_conditions", nullable = true, columnDefinition = "TEXT")
    private String termsConditions;

    @Column(name = "created_by", nullable = true)
    private String createdBy;

    @Column(name = "created_date", nullable = true)
    private Timestamp createdDate;

    @Column(name = "modified_by", nullable = true)
    private String modifiedBy;

    @Column(name = "modified_date", nullable = true)
    private Timestamp modifiedDate;

    @Column(name = "auto_check_out", nullable = true)
    private Boolean autoCheckOut;

    @Column(name = "auto_check_out_time", nullable = true)
    private LocalTime autoCheckOutTime;

    @Column(name = "is_deleted", nullable = true)
    @Builder.Default
    private Boolean isDeleted = false;

}

