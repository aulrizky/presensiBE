package com.tujuhsembilan.presensi79.repository;

import com.tujuhsembilan.presensi79.model.CompanyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyConfigRepository extends JpaRepository<CompanyConfig, Integer> {
    CompanyConfig findByCompany_IdCompany(Integer idCompany);
}
