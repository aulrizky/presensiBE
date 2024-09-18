package com.tujuhsembilan.presensi79.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.tujuhsembilan.presensi79.model.Attendance;

@Repository
@EnableJpaRepositories
public interface AttendanceRepository extends JpaRepository<Attendance, Integer>{
    List<Attendance> findByEmployee_IdEmployee(Integer idEmployee);
    List<Attendance> findByEmployee_IdEmployeeAndDateAttendance(Integer idEmployee, LocalDate dateAttendance);
    List<Attendance> findByEmployee_IdEmployeeAndDateAttendanceBetween(Integer idEmployee, LocalDate startDate, LocalDate endDate);
    List<Attendance> findByEmployee_IdEmployeeOrderByDateAttendanceDesc(Integer idEmployee);

    @Query("SELECT a FROM Attendance a WHERE a.dateAttendance >= :startDateFilter AND a.dateAttendance <= :endDateFilter")
    List<Attendance> findAllAttendanceByDate(LocalDate startDateFilter, LocalDate endDateFilter);
}
