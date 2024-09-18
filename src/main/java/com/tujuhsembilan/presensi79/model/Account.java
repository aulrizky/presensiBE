package com.tujuhsembilan.presensi79.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "Account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "id_account", nullable = false, updatable = false)
    @Builder.Default
    private String idAccount = UUID.randomUUID().toString();

    @Column(name = "username", nullable = false, unique = true)
    @Size(min = 7, max = 20)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "created_by", nullable = true)
    private String createdBy;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = true)
    private Timestamp createdDate;

    @Column(name = "modified_by", nullable = true)
    private String modifiedBy;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date", nullable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted", nullable = true)
    @Builder.Default
    private Boolean isDeleted = false;
}

