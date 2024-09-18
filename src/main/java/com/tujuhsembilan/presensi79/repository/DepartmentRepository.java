package com.tujuhsembilan.presensi79.repository;

import com.tujuhsembilan.presensi79.model.Department;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findById(Integer idDepartment);
    List<Department> findByCompany_IdCompany(Integer idCompany);
}
