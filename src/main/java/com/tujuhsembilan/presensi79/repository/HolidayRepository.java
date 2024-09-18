package com.tujuhsembilan.presensi79.repository;

import com.tujuhsembilan.presensi79.model.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Integer> {
    Page<Holiday> findByCompany_IdCompany(Integer companyId, Pageable pageable);
}
