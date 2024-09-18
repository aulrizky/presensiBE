package com.tujuhsembilan.presensi79.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tujuhsembilan.presensi79.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>, JpaSpecificationExecutor<Admin> {

    @Query("SELECT COUNT(f) FROM Admin f WHERE f.company.idCompany= :companyId")
    long countByCompanyId(@Param("companyId") Integer companyId);

    // Tambahkan query untuk mendapatkan daftar admin yang tidak dihapus
    @Query("SELECT a FROM Admin a WHERE a.isDeleted = false")
    Page<Admin> findAdminsByIsDeletedFalse(Pageable pageable);

}
