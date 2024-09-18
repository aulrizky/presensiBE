package com.tujuhsembilan.presensi79.model;

import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Leave")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_leave", nullable = false, unique = true, updatable = false)
    private Integer idLeave;

    @ManyToOne
    @JoinColumn(name = "id_employee", referencedColumnName = "id_employee")
    private Employee employee;

    @Column(name = "leave_title", nullable = false)
    @NotNull
    @Size(min = 3, max = 20)
    private String leaveTitle;

    @Column(name = "leave_type", nullable = false)
    @NotNull
    private String leaveType;

    @Column(name = "start_date", nullable = false)
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull
    private LocalDate endDate;

    @Column(name = "reason", nullable = false)
    @NotNull
    @Size(min = 3, max = 255)
    private String reason;

    @Column(name = "attachment", nullable = true)
    private String attachment;

    @Column(name = "status", nullable = false)
    private String status;

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

