package com.tujuhsembilan.presensi79.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Attendance")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_attendance", nullable = false, unique = true, updatable = false)
    private Integer idAttendance;

    @ManyToOne
    @JoinColumn(name = "id_employee", referencedColumnName = "id_employee")
    private Employee employee;

    @Column(name = "check_in", nullable = false)
    private Timestamp checkIn;

    @Column(name = "check_out", nullable = true)
    private Timestamp checkOut;

    @Column(name = "total_working_hours", nullable = true)
    private LocalTime totalWorkingHours;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "check_in_proof", nullable = true)
    private String checkInProof;

    @Column(name = "check_out_proof", nullable = true)
    private String checkOutProof;

    @Column(name = "check_out_note", nullable = true)
    private String checkOutNote;

    @Column(name = "date_attendance", nullable = false)
    private LocalDate dateAttendance;

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

