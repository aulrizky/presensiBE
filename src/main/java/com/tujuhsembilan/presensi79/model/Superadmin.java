package com.tujuhsembilan.presensi79.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "Superadmin")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Superadmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_superadmin", nullable = false, unique = true, updatable = false)
    private Integer idSuperadmin;

    @OneToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id_account")
    private Account account;

    @Column(name = "name", nullable = false)
    private String name;

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

