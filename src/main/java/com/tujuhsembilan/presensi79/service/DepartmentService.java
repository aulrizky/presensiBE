package com.tujuhsembilan.presensi79.service;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.response.DepartmentResponse;
import com.tujuhsembilan.presensi79.model.Department;
import com.tujuhsembilan.presensi79.repository.DepartmentRepository;
import com.tujuhsembilan.presensi79.util.MessageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final MessageUtil messageUtil;

    @Transactional
    public MessageResponse getDepartmentsByCompany(Integer idCompany) {
        try {
            List<Department> departments = departmentRepository.findByCompany_IdCompany(idCompany);
            if (departments.isEmpty()) {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Departments"),
                    HttpStatus.NOT_FOUND.value(),
                    messageUtil.get("application.status.notfound")
                );
            } else {
                List<DepartmentResponse> departmentResponses = departments.stream()
                    .map(department -> DepartmentResponse.builder()
                        .idDepartment(department.getIdDepartment())
                        .departmentName(department.getDepartmentName())
                        .isDeleted(department.getIsDeleted())
                        .build()
                    ).collect(Collectors.toList());

                return new MessageResponse(
                    messageUtil.get("application.success.retrieve", "Departments"),
                    HttpStatus.OK.value(),
                    messageUtil.get("application.status.ok"),
                    departmentResponses
                );
            }
        } catch (Exception e) {
            return new MessageResponse(
                messageUtil.get("application.error.internal"),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageUtil.get("application.status.error"),
                e.getMessage()
            );
        }
    }
}
