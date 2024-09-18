package com.tujuhsembilan.presensi79.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "Employee")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employee", nullable = false, unique = true, updatable = false)
    private Integer idEmployee;

    @OneToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id_account")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "id_company", referencedColumnName = "id_company")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "id_department", referencedColumnName = "id_department")
    private Department department;

    @Column(name = "employee_number", nullable = false)
    @Size(min = 7, max = 20)
    @NotNull
    private String employeeNumber;

    @Column(name = "first_name", nullable = false)
    @Size(min = 2, max = 50)
    @NotNull
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(min = 2, max = 50)
    @NotNull
    private String lastName;

    @Column(name = "date_of_birth", nullable = true)
    private LocalDate dateOfBirth;

    @Column(name = "mobile_number", nullable = true)
    @Size(min = 10, max = 13)
    private String mobileNumber;

    @Column(name = "gender", nullable = true)
    private String gender;

    @Column(name = "marital_status", nullable = true)
    private String maritalStatus;

    @Column(name = "nationality", nullable = true)
    private String nationality;

    @Column(name = "address", nullable = true)
    @Size(min = 5, max = 255)
    private String address;

    @Column(name = "province", nullable = true)
    private String province;

    @Column(name = "city", nullable = true)
    private String city;

    @Column(name = "district", nullable = true)
    private String district;

    @Column(name = "zip_code", nullable = true)
    private String zipCode;

    @Column(name = "status", nullable = true)
    private String status;

    @Column(name = "email", nullable = false)
    @Email
    @NotNull
    private String email;

    @Column(name = "role_current_company", nullable = false)
    private String roleCurrentCompany;

    @Column(name = "role_in_client", nullable = true)
    private String roleInClient;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Column(name = "profile_picture", nullable = true)
    private String profilePicture;

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
