package com.tujuhsembilan.presensi79.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.CheckInRequest;
import com.tujuhsembilan.presensi79.dto.request.CheckOutRequest;
import com.tujuhsembilan.presensi79.dto.response.AttendanceResponse;
import com.tujuhsembilan.presensi79.dto.response.CheckInResponse;
import com.tujuhsembilan.presensi79.dto.response.DaysWorkedResponse;
import com.tujuhsembilan.presensi79.model.Attendance;
import com.tujuhsembilan.presensi79.model.CompanyConfig;
import com.tujuhsembilan.presensi79.model.Employee;
import com.tujuhsembilan.presensi79.repository.AttendanceRepository;
import com.tujuhsembilan.presensi79.repository.CompanyConfigRepository;
import com.tujuhsembilan.presensi79.repository.EmployeeRepository;
import com.tujuhsembilan.presensi79.util.AttendanceUtil;
import com.tujuhsembilan.presensi79.util.MessageUtil;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final CompanyConfigRepository companyConfigRepository;
    private final MessageUtil messageUtil;

    public MessageResponse getAttendanceDetailsByEmployee(Integer idEmployee) {
        try {
            List<Attendance> attendances = attendanceRepository.findByEmployee_IdEmployeeOrderByDateAttendanceDesc(idEmployee);
            if (attendances != null && !attendances.isEmpty()) {
                List<AttendanceResponse> responses = attendances.stream()
                        .map(attendance -> AttendanceResponse.builder()
                                .idAttendance(attendance.getIdAttendance())
                                .idEmployee(attendance.getEmployee().getIdEmployee())
                                .dateAttendance(attendance.getDateAttendance())
                                .checkIn(attendance.getCheckIn().toLocalDateTime())  // Menggunakan LocalDateTime dari database
                                .checkOut(attendance.getCheckOut() != null ? attendance.getCheckOut().toLocalDateTime() : null)  // Menggunakan LocalDateTime dari database jika ada
                                .totalWorkingHours(attendance.getTotalWorkingHours())
                                .status(attendance.getStatus())
                                .checkInProof(attendance.getCheckInProof())
                                .checkOutProof(attendance.getCheckOutProof())
                                .checkOutNote(attendance.getCheckOutNote())
                                .isDeleted(attendance.getIsDeleted())
                                .build())
                        .collect(Collectors.toList());
    
                return new MessageResponse(messageUtil.get("application.success.retrieve.all", "attendance details"), HttpStatus.OK.value(), messageUtil.get("application.status.ok"), responses);
            } else {
                return new MessageResponse(messageUtil.get("application.error.notfound", "Attendance details"), HttpStatus.NOT_FOUND.value(), messageUtil.get("application.status.notfound"));
            }
        } catch (Exception e) {
            logger.error("Error: {}", e);
            return new MessageResponse(messageUtil.get("application.error.internal"), HttpStatus.INTERNAL_SERVER_ERROR.value(), messageUtil.get("application.status.error"), e.getMessage());
        }
    }
    
    public MessageResponse checkInAttendance(Integer idEmployee, CheckInRequest request) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(idEmployee);
            if (!employeeOptional.isPresent()) {
                return new MessageResponse(messageUtil.get("application.error.notfound", "Employee"), HttpStatus.NOT_FOUND.value(), messageUtil.get("application.status.notfound"));
            }

            Employee employee = employeeOptional.get();
            String username = employee.getAccount().getUsername();

            // Logging waktu check-in yang diterima
            logger.info("Check-in time: {}", request.getCheckInTime());

            // Cek apakah sudah check-in pada tanggal yang sama
            List<Attendance> existingAttendances = attendanceRepository.findByEmployee_IdEmployeeAndDateAttendance(idEmployee, request.getDate());
            if (existingAttendances != null && !existingAttendances.isEmpty()) {
                return new MessageResponse(messageUtil.get("application.error.multiple", "Attendance on the same day"), HttpStatus.BAD_REQUEST.value(), messageUtil.get("application.status.badrequest"));
            }

            // Mengambil konfigurasi perusahaan
            CompanyConfig companyConfig = companyConfigRepository.findByCompany_IdCompany(employee.getCompany().getIdCompany());
            if (companyConfig == null) {
                return new MessageResponse(messageUtil.get("application.error.notfound", "Company configuration"), HttpStatus.NOT_FOUND.value(), messageUtil.get("application.status.notfound"));
            }

            // Periksa apakah check-in terlalu awal
            LocalTime checkInLocalTime = request.getCheckInTime().toLocalTime();
            LocalTime workingHoursStart = companyConfig.getWorkingHoursStart();
            LocalTime earliestCheckInTime = workingHoursStart.minusMinutes(companyConfig.getCheckInTolerance());

            if (checkInLocalTime.isBefore(earliestCheckInTime)) {
                return new MessageResponse(messageUtil.get("application.error.earlycheckin", "Check-in too early"), HttpStatus.BAD_REQUEST.value(), messageUtil.get("application.status.badrequest"));
            }

            // Menghitung status menggunakan util
            String status = AttendanceUtil.calculateStatus(request.getCheckInTime(), companyConfig);

            Attendance attendance = Attendance.builder()
                .employee(employee)
                .dateAttendance(request.getDate())
                .checkIn(Timestamp.valueOf(request.getCheckInTime()))
                .status(status)
                .createdBy(username)
                .createdDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))))
                .modifiedBy(username)
                .modifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))))
                .build();

            Attendance savedAttendance = attendanceRepository.save(attendance);

            // Pastikan respons mengambil waktu dari database
            CheckInResponse response = CheckInResponse.builder()
                .idAttendance(savedAttendance.getIdAttendance())
                .idEmployee(savedAttendance.getEmployee().getIdEmployee())
                .dateAttendance(savedAttendance.getDateAttendance())
                .checkIn(savedAttendance.getCheckIn().toLocalDateTime())
                .status(savedAttendance.getStatus())
                .build();

            return new MessageResponse(messageUtil.get("application.success.checkin"), HttpStatus.CREATED.value(), messageUtil.get("application.status.created"), response);
        } catch (Exception e) {
            return new MessageResponse(messageUtil.get("application.error.internal"), HttpStatus.INTERNAL_SERVER_ERROR.value(), messageUtil.get("application.status.error"), e.getMessage());
        }
    }
    

    public MessageResponse checkOutAttendance(Integer idEmployee, CheckOutRequest request) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(idEmployee);
            if (!employeeOptional.isPresent()) {
                return new MessageResponse(messageUtil.get("application.error.notfound", "Employee"), HttpStatus.NOT_FOUND.value(), messageUtil.get("application.status.notfound"));
            }
    
            Employee employee = employeeOptional.get();
            String username = employee.getAccount().getUsername();
    
            logger.info("Check-out time: {}", request.getCheckOutTime());
    
            List<Attendance> attendances = attendanceRepository.findByEmployee_IdEmployeeAndDateAttendance(idEmployee, request.getDate());
            if (attendances == null || attendances.isEmpty()) {
                return new MessageResponse(messageUtil.get("application.error.notfound", "Attendance"), HttpStatus.NOT_FOUND.value(), messageUtil.get("application.status.notfound"));
            } else if (attendances.size() > 1) {
                return new MessageResponse(messageUtil.get("application.error.multiple", "Attendances"), HttpStatus.INTERNAL_SERVER_ERROR.value(), messageUtil.get("application.status.error"));
            }
    
            Attendance attendance = attendances.get(0);
    
            if (attendance.getCheckOut() != null) {
                return new MessageResponse(messageUtil.get("application.error.already.exists", "Check-out for this date"), HttpStatus.BAD_REQUEST.value(), messageUtil.get("application.status.badrequest"));
            }
    
            CompanyConfig companyConfig = companyConfigRepository.findByCompany_IdCompany(employee.getCompany().getIdCompany());
            if (companyConfig == null) {
                return new MessageResponse(messageUtil.get("application.error.notfound", "Company configuration"), HttpStatus.NOT_FOUND.value(), messageUtil.get("application.status.notfound"));
            }
    
            boolean isNoteRequired = AttendanceUtil.isNoteRequired(request.getCheckOutTime(), companyConfig);

            if (isNoteRequired && (request.getCheckOutNote() == null || request.getCheckOutNote().isEmpty())) {
                return new MessageResponse(messageUtil.get("application.error.missing", "Check-out note"), HttpStatus.BAD_REQUEST.value(), messageUtil.get("application.status.badrequest"));
            }
    
            attendance.setCheckOut(Timestamp.valueOf(request.getCheckOutTime()));
    
            long breakTimeMinutes = companyConfig.getBreakTime() != null ? companyConfig.getBreakTime() : 0;
            LocalTime totalWorkingHours = AttendanceUtil.calculateTotalWorkingHours(attendance.getCheckIn().toLocalDateTime(), request.getCheckOutTime(), breakTimeMinutes);
    
            attendance.setCheckOutNote(request.getCheckOutNote());
            attendance.setTotalWorkingHours(totalWorkingHours);
            attendance.setModifiedBy(username);
            attendance.setModifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))));
    
            Attendance updatedAttendance = attendanceRepository.save(attendance);
    
            AttendanceResponse response = AttendanceResponse.builder()
                    .idAttendance(updatedAttendance.getIdAttendance())
                    .idEmployee(updatedAttendance.getEmployee().getIdEmployee())
                    .dateAttendance(updatedAttendance.getDateAttendance())
                    .checkIn(updatedAttendance.getCheckIn().toLocalDateTime())  
                    .checkOut(updatedAttendance.getCheckOut().toLocalDateTime()) 
                    .status(updatedAttendance.getStatus())
                    .totalWorkingHours(updatedAttendance.getTotalWorkingHours())
                    .checkOutNote(updatedAttendance.getCheckOutNote())
                    .build();
    
            return new MessageResponse(messageUtil.get("application.success.checkout"), HttpStatus.OK.value(), "OK", response);
        } catch (Exception e) {
            logger.error("Error: {}", e);
            return new MessageResponse(messageUtil.get("application.error.internal"), HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR", e.getMessage());
        }
    }

    public void autoCheckOutEmployees() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));
        logger.info("Running auto check-out at {}", now);
        List<CompanyConfig> companyConfigs = companyConfigRepository.findAll();

        for (CompanyConfig config : companyConfigs) {
            logger.info("Checking company config for company ID {}", config.getCompany().getIdCompany());
            if (Boolean.TRUE.equals(config.getAutoCheckOut()) && config.getAutoCheckOutTime() != null) {
                LocalTime autoCheckOutTime = config.getAutoCheckOutTime();
                logger.info("Auto check-out time for company ID {} is {}", config.getCompany().getIdCompany(), autoCheckOutTime);
                if (now.toLocalTime().isAfter(autoCheckOutTime) || now.toLocalTime().equals(autoCheckOutTime)) {
                    List<Employee> employees = employeeRepository.findAll();
                    for (Employee employee : employees) {
                        autoCheckOutEmployee(employee, config, now);
                    }
                }
            }
        }
    }

    private void autoCheckOutEmployee(Employee employee, CompanyConfig config, LocalDateTime now) {
        try {
            List<Attendance> attendances = attendanceRepository.findByEmployee_IdEmployeeAndDateAttendance(
                    employee.getIdEmployee(), now.toLocalDate());
            if (attendances.isEmpty()) {
                logger.info("No attendance record found for employee ID {} on date {}", employee.getIdEmployee(), now.toLocalDate());
                return;
            }
            Attendance attendance = attendances.get(0);
            if (attendance.getCheckOut() != null) {
                logger.info("Employee ID {} has already checked out for date {}", employee.getIdEmployee(), now.toLocalDate());
                return;
            }

            long breakTimeMinutes = config.getBreakTime() != null ? config.getBreakTime() : 0;
            LocalTime totalWorkingHours = AttendanceUtil.calculateTotalWorkingHours(attendance.getCheckIn().toLocalDateTime(), now, breakTimeMinutes);

            attendance.setCheckOut(Timestamp.valueOf(now));
            attendance.setCheckOutNote("Auto check-out");
            attendance.setTotalWorkingHours(totalWorkingHours);
            attendance.setModifiedBy("System");
            attendance.setModifiedDate(Timestamp.valueOf(now));

            attendanceRepository.save(attendance);
            logger.info("Auto checked out employee ID {} at {}", employee.getIdEmployee(), now);
        } catch (Exception e) {
            logger.error("Error auto checking out employee ID {}: {}", employee.getIdEmployee(), e.getMessage());
        }
    }

    public MessageResponse getDaysWorkedByEmployee(Integer idEmployee, LocalDate currentDate) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(idEmployee);
            if (!employeeOptional.isPresent()) {
                return new MessageResponse(messageUtil.get("application.error.notfound", "Employee"), HttpStatus.NOT_FOUND.value(), messageUtil.get("application.status.notfound"));
            }
            
            List<Attendance> attendances = attendanceRepository.findByEmployee_IdEmployeeAndDateAttendanceBetween(
                    idEmployee, currentDate.withDayOfMonth(1), currentDate.withDayOfMonth(currentDate.lengthOfMonth()));
            int daysWorked = attendances.size();

            DaysWorkedResponse response = DaysWorkedResponse.builder()
                    .daysWorked(daysWorked)
                    .build();

            return new MessageResponse(messageUtil.get("application.success.retrieve", "Days worked"), HttpStatus.OK.value(), messageUtil.get("application.status.ok"), response);
        } catch (Exception e) {
            logger.error("Error: {}", e);
            return new MessageResponse(messageUtil.get("application.error.internal"), HttpStatus.INTERNAL_SERVER_ERROR.value(), messageUtil.get("application.status.error"), e.getMessage());
        }
    }
}
