package com.tujuhsembilan.presensi79.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "Holiday")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_holiday", nullable = false, unique = true, updatable = false)
    private Integer idHoliday;

    @ManyToOne
    @JoinColumn(name = "id_company", referencedColumnName = "id_company")
    private Company company;

    @Column(name = "holiday_name", nullable = false)
    private String holidayName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "created_by", nullable = true)
    private String createdBy;

    @Column(name = "created_date", nullable = true)
    private Timestamp createdDate;

    @Column(name = "modified_by", nullable = true)
    private String modifiedBy;

    @Column(name = "modified_date", nullable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted", nullable = true)
    @Builder.Default
    private Boolean isDeleted = false;
}

