package com.tujuhsembilan.presensi79.repository;

import com.tujuhsembilan.presensi79.model.Leave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Integer> {
    Page<Leave> findByEmployee_IdEmployee(Integer idEmployee, Pageable pageable);
}
