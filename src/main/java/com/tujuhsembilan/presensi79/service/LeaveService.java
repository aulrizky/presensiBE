package com.tujuhsembilan.presensi79.service;

import com.tujuhsembilan.presensi79.config.minio.MinioService;
import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.LeaveRequest;
import com.tujuhsembilan.presensi79.dto.response.ApplyLeaveResponse;
import com.tujuhsembilan.presensi79.dto.response.LeaveCardResponse;
import com.tujuhsembilan.presensi79.model.Employee;
import com.tujuhsembilan.presensi79.model.Leave;
import com.tujuhsembilan.presensi79.repository.EmployeeRepository;
import com.tujuhsembilan.presensi79.repository.LeaveRepository;
import com.tujuhsembilan.presensi79.util.MessageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final MessageUtil messageUtil;
    private final MinioService minioService;

    private static final Logger logger = LoggerFactory.getLogger(LeaveService.class);

    public MessageResponse getAllLeaves(Integer idEmployee, int page, int size) {
        try {
            logger.info("Fetching leaves for employee ID: {}", idEmployee);

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "idLeave"));
            Page<Leave> leavesPage = leaveRepository.findByEmployee_IdEmployee(idEmployee, pageable);

            logger.info("Leaves Page: {}", leavesPage);

            List<Leave> leaves = leavesPage.getContent();
            logger.info("Fetched Leaves: {}", leaves);

            if (!leaves.isEmpty()) {
                List<LeaveCardResponse> response = leaves.stream().map(leave -> {
                    LeaveCardResponse leaveCardResponse = LeaveCardResponse.builder()
                        .idLeave(leave.getIdLeave())
                        .idEmployee(leave.getEmployee().getIdEmployee())
                        .leaveTitle(leave.getLeaveTitle())
                        .leaveType(leave.getLeaveType())
                        .startDate(leave.getStartDate())
                        .endDate(leave.getEndDate())
                        .status(leave.getStatus())
                        .isDeleted(leave.getIsDeleted())
                        .build();
                    logger.info("Mapped LeaveCardResponse: {}", leaveCardResponse);
                    return leaveCardResponse;
                }).collect(Collectors.toList());

                return new MessageResponse(
                        messageUtil.get("application.success.retrieve", "Leaves"),
                        HttpStatus.OK.value(),
                        messageUtil.get("application.status.ok"),
                        response
                );
            } else {
                logger.warn("No leaves found for employee ID: {}", idEmployee);
                return new MessageResponse(
                        messageUtil.get("application.error.notfound", "Leaves"),
                        HttpStatus.NOT_FOUND.value(),
                        messageUtil.get("application.status.notfound")
                );
            }
        } catch (Exception e) {
            logger.error("Error fetching all leaves for employee {}: {}", idEmployee, e.getMessage(), e);
            return new MessageResponse(
                    messageUtil.get("application.error.internal"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    messageUtil.get("application.status.error"),
                    e.getMessage()
            );
        }
    }

    public MessageResponse applyLeave(Integer idEmployee, LeaveRequest leaveRequest, MultipartFile attachment) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(idEmployee);
            if (!employeeOptional.isPresent()) {
                logger.warn("Employee not found for ID: {}", idEmployee);
                return new MessageResponse(
                        messageUtil.get("application.error.notfound", "Employee"),
                        HttpStatus.NOT_FOUND.value(),
                        messageUtil.get("application.status.notfound")
                );
            }

            Employee employee = employeeOptional.get();
            String username = employee.getAccount().getUsername();

            // Validate dates
            if (leaveRequest.getEndDate() == null || leaveRequest.getStartDate() == null) {
                logger.warn("Invalid dates in leave request: {}", leaveRequest);
                return new MessageResponse(
                        messageUtil.get("application.error.invalid", "Start date and end date cannot be null"),
                        HttpStatus.BAD_REQUEST.value(),
                        messageUtil.get("application.status.badrequest")
                );
            }

            if (leaveRequest.getEndDate().isBefore(leaveRequest.getStartDate())) {
                logger.warn("End date is before start date in leave request: {}", leaveRequest);
                return new MessageResponse(
                        messageUtil.get("application.error.invalid", "End date cannot be before start date"),
                        HttpStatus.BAD_REQUEST.value(),
                        messageUtil.get("application.status.badrequest")
                );
            }

            // Handle file upload
            String attachmentFilename = null;
            String attachmentUrl = null;
            if (attachment != null && !attachment.isEmpty()) {
                attachmentFilename = minioService.uploadFileToMinio(attachment, "leave_attachment_" + employee.getIdEmployee());
                attachmentUrl = minioService.getPublicLink(attachmentFilename);
            }

            // Create new Leave object
            Leave leave = Leave.builder()
                    .employee(employee)
                    .leaveTitle(leaveRequest.getLeaveTitle())
                    .leaveType(leaveRequest.getLeaveType())
                    .startDate(leaveRequest.getStartDate())
                    .endDate(leaveRequest.getEndDate())
                    .reason(leaveRequest.getReason())
                    .attachment(attachmentFilename)
                    .status("Pending")
                    .createdBy(username)
                    .createdDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))))
                    .modifiedBy(username)
                    .modifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))))
                    .build();

            Leave savedLeave = leaveRepository.save(leave);

            ApplyLeaveResponse applyLeaveResponse = ApplyLeaveResponse.builder()
                    .idLeave(savedLeave.getIdLeave())
                    .idEmployee(savedLeave.getEmployee().getIdEmployee())
                    .leaveTitle(savedLeave.getLeaveTitle())
                    .leaveType(savedLeave.getLeaveType())
                    .startDate(savedLeave.getStartDate())
                    .endDate(savedLeave.getEndDate())
                    .reason(savedLeave.getReason())
                    .attachment(attachmentUrl)
                    .status(savedLeave.getStatus())
                    .build();

            return new MessageResponse(
                    messageUtil.get("application.success.create", "Leave"),
                    HttpStatus.CREATED.value(),
                    messageUtil.get("application.status.created"),
                    applyLeaveResponse
            );
        } catch (Exception e) {
            logger.info("Error applying leave for employee {}: {}", idEmployee, e.getMessage(), e);
            return new MessageResponse(
                    messageUtil.get("application.error.internal"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    messageUtil.get("application.status.error"),
                    e.getMessage()
            );
        }
    }
}
