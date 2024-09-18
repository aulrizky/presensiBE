package com.tujuhsembilan.presensi79.repository;

import com.tujuhsembilan.presensi79.model.Account;
import com.tujuhsembilan.presensi79.model.Employee;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByAccount(Account account);
    List<Employee> findAllByCompany_IdCompany(Integer idCompany);
}
