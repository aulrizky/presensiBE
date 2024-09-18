package com.tujuhsembilan.presensi79.service;

import com.tujuhsembilan.presensi79.config.minio.MinioService;
import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.EditProfileRequest;
import com.tujuhsembilan.presensi79.dto.request.EmployeeRegistrationRequest;
import com.tujuhsembilan.presensi79.dto.request.LoginRequest;
import com.tujuhsembilan.presensi79.dto.response.EditProfileResponse;
import com.tujuhsembilan.presensi79.dto.response.EmployeePersonalInformationResponse;
import com.tujuhsembilan.presensi79.dto.response.EmployeeProfessionalInformationResponse;
import com.tujuhsembilan.presensi79.dto.response.EmployeeProfileResponse;
import com.tujuhsembilan.presensi79.dto.response.EmployeeRegistrationResponse;
import com.tujuhsembilan.presensi79.dto.response.LoginResponse;
import com.tujuhsembilan.presensi79.model.Account;
import com.tujuhsembilan.presensi79.model.Company;
import com.tujuhsembilan.presensi79.model.Department;
import com.tujuhsembilan.presensi79.model.Employee;
import com.tujuhsembilan.presensi79.repository.AccountRepository;
import com.tujuhsembilan.presensi79.repository.CompanyRepository;
import com.tujuhsembilan.presensi79.repository.DepartmentRepository;
import com.tujuhsembilan.presensi79.repository.EmployeeRepository;
import com.tujuhsembilan.presensi79.security.jwt.JwtUtils;
import com.tujuhsembilan.presensi79.util.MessageUtil;

import java.io.IOException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeService {

    private final Validator validator;
    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final MessageUtil messageUtil;
    private final MinioService minioService;

    @Transactional
    public MessageResponse registerEmployee(EmployeeRegistrationRequest request) {
        try {
            Set<ConstraintViolation<EmployeeRegistrationRequest>> constraintViolations = validator.validate(request);
    
            if (!constraintViolations.isEmpty()) {
                ConstraintViolation<EmployeeRegistrationRequest> firstViolation = constraintViolations.iterator().next();
                String errorMessage = firstViolation.getMessage();
                return new MessageResponse(errorMessage, HttpStatus.BAD_REQUEST.value(), messageUtil.get("application.status.error"));
            }
    
            if (accountRepository.existsByUsername(request.getUsername())) {
                return new MessageResponse(
                    messageUtil.get("application.error.already.exists", "Username"),
                    HttpStatus.BAD_REQUEST.value(),
                    messageUtil.get("application.status.error")
                );
            }
    
            Optional<Company> optionalCompany = companyRepository.findById(request.getIdCompany());
            if (!optionalCompany.isPresent()) {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Company"),
                    HttpStatus.BAD_REQUEST.value(),
                    messageUtil.get("application.status.error")
                );
            }
    
            Optional<Department> optionalDepartment = departmentRepository.findById(request.getIdDepartment());
            if (!optionalDepartment.isPresent()) {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Department"),
                    HttpStatus.BAD_REQUEST.value(),
                    messageUtil.get("application.status.error")
                );
            }
    
            Account account = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("Employee")
                .createdBy("Admin")
                .createdDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))))
                .modifiedBy("Admin")
                .modifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))))
                .build();
    
            Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .employeeNumber(request.getEmployeeNumber())
                .company(optionalCompany.get())
                .department(optionalDepartment.get())
                .roleCurrentCompany(request.getRoleCurrentCompany())
                .joiningDate(LocalDate.now(ZoneId.of("Asia/Jakarta")))
                .createdBy("Admin")
                .createdDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))))
                .modifiedBy("Admin")
                .modifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))))
                .build();
    
            Account savedAccount = accountRepository.save(account);
            employee.setAccount(savedAccount);
            Employee savedEmployee = employeeRepository.save(employee);
    
            EmployeeRegistrationResponse employeeResponse = EmployeeRegistrationResponse.builder()
                .idEmployee(savedEmployee.getIdEmployee())
                .idAccount(savedAccount.getIdAccount())
                .idCompany(savedEmployee.getCompany().getIdCompany())
                .idDepartment(savedEmployee.getDepartment().getIdDepartment())
                .employeeNumber(savedEmployee.getEmployeeNumber())
                .firstName(savedEmployee.getFirstName())
                .lastName(savedEmployee.getLastName())
                .username(savedAccount.getUsername())
                .email(savedEmployee.getEmail())
                .role(savedAccount.getRole())
                .roleCurrentCompany(savedEmployee.getRoleCurrentCompany())
                .build();
    
            return new MessageResponse(
                messageUtil.get("application.success.create", "Employee"),
                HttpStatus.CREATED.value(),
                messageUtil.get("application.status.ok"),
                employeeResponse
            );
        } catch (Exception e) {
            return new MessageResponse(
                messageUtil.get("application.error.internal"),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageUtil.get("application.status.error"),
                e.getMessage()
            );
        }
    }

    @Transactional
    public MessageResponse loginEmployee(LoginRequest request) {
        try {
            Optional<Account> accountOptional = accountRepository.findByUsername(request.getUsername());
    
            if (!accountOptional.isPresent() || !passwordEncoder.matches(request.getPassword(), accountOptional.get().getPassword())) {
                return new MessageResponse(
                    messageUtil.get("application.error.login"),
                    HttpStatus.UNAUTHORIZED.value(),
                    messageUtil.get("application.status.unauthorized")
                );
            }
    
            Account account = accountOptional.get();
            Employee employee = employeeRepository.findByAccount(account);
    
            Map<String, Object> claims = Map.of(
                "id_account", account.getIdAccount(),
                "id_employee", employee.getIdEmployee(),
                "id_company", employee.getCompany().getIdCompany()
            );
    
            String token = jwtUtils.generateJwtToken(account.getUsername(), claims);
    
            LoginResponse loginResponse = LoginResponse.builder()
                .idAccount(account.getIdAccount())
                .idEmployee(employee.getIdEmployee())
                .idCompany(employee.getCompany().getIdCompany())
                .token(token)
                .type("Bearer")
                .username(account.getUsername())
                .role(account.getRole())
                .build();
    
            return new MessageResponse(
                messageUtil.get("application.success.login"),
                HttpStatus.OK.value(),
                messageUtil.get("application.status.ok"),
                loginResponse
            );
        } catch (Exception e) {
            return new MessageResponse(
                messageUtil.get("application.error.internal"),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageUtil.get("application.status.error"),
                e.getMessage()
            );
        }
    }

    public MessageResponse getEmployeeProfile(Integer idEmployee) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(idEmployee);
    
            if (!optionalEmployee.isPresent()) {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Employee"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    messageUtil.get("application.status.error"),
                    "Employee with ID " + idEmployee + " not found"
                );
            }
    
            Employee employee = optionalEmployee.get();
            String profilePictureUrl = employee.getProfilePicture() != null ? minioService.getPublicLink(employee.getProfilePicture()) : null;

            EmployeeProfileResponse profileResponse = EmployeeProfileResponse.builder()
                .idEmployee(employee.getIdEmployee())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .roleCurrentCompany(employee.getRoleCurrentCompany())
                .profilePicture(profilePictureUrl)
                .idCompany(employee.getCompany().getIdCompany())
                .build();
    
            return new MessageResponse(
                messageUtil.get("application.success.retrieve", "Employee profile"),
                HttpStatus.OK.value(),
                messageUtil.get("application.status.ok"),
                profileResponse
            );
        } catch (Exception e) {
            return new MessageResponse(
                messageUtil.get("application.error.internal"),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageUtil.get("application.status.error"),
                e.getMessage()
            );
        }
    }

    public MessageResponse getEmployeePersonalInformation(Integer idEmployee) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(idEmployee);
    
            if (!optionalEmployee.isPresent()) {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Employee"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    messageUtil.get("application.status.error"),
                    "Employee with ID " + idEmployee + " not found"
                );
            }
    
            Employee employee = optionalEmployee.get();
            
            EmployeePersonalInformationResponse personalInformationResponse = EmployeePersonalInformationResponse.builder()
                .idEmployee(employee.getIdEmployee())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .dateOfBirth(employee.getDateOfBirth())
                .gender(employee.getGender())
                .maritalStatus(employee.getMaritalStatus())
                .mobileNumber(employee.getMobileNumber())
                .nationality(employee.getNationality())
                .address(employee.getAddress())
                .province(employee.getProvince())
                .city(employee.getCity())
                .district(employee.getDistrict())
                .zipCode(employee.getZipCode())
                .build();
    
            return new MessageResponse(
                messageUtil.get("application.success.retrieve", "Employee profile"),
                HttpStatus.OK.value(),
                messageUtil.get("application.status.ok"),
                personalInformationResponse
            );
        } catch (Exception e) {
            return new MessageResponse(
                messageUtil.get("application.error.internal"),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageUtil.get("application.status.error"),
                e.getMessage()
            );
        }
    }

    public MessageResponse getEmployeeProfessionalInformation(Integer idEmployee) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(idEmployee);
    
            if (!optionalEmployee.isPresent()) {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Employee"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    messageUtil.get("application.status.error"),
                    "Employee with ID " + idEmployee + " not found"
                );
            }
    
            Employee employee = optionalEmployee.get();
            Account account = employee.getAccount();
            Integer idDepartment = employee.getDepartment().getIdDepartment();
            Optional<Department> optionalDepartment = departmentRepository.findById(idDepartment);

            if (!optionalDepartment.isPresent()) {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Department"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    messageUtil.get("application.status.error")
                );
            }

            
            Department department = optionalDepartment.get();

            EmployeeProfessionalInformationResponse professionalInformationResponse = EmployeeProfessionalInformationResponse.builder()
                .idEmployee(employee.getIdEmployee())
                .employeeNumber(employee.getEmployeeNumber())
                .username(account.getUsername())
                .email(employee.getEmail())
                .idDepartment(idDepartment)
                .departmentName(department.getDepartmentName())
                .status(employee.getStatus())
                .role(employee.getRoleInClient())
                .roleCurrentCompany(employee.getRoleCurrentCompany())
                .joiningDate(employee.getJoiningDate())
                .build();
    
            return new MessageResponse(
                messageUtil.get("application.success.retrieve", "Employee profile"),
                HttpStatus.OK.value(),
                messageUtil.get("application.status.ok"),
                professionalInformationResponse
            );
        } catch (Exception e) {
            return new MessageResponse(
                messageUtil.get("application.error.internal"),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageUtil.get("application.status.error"),
                e.getMessage()
            );
        }
    }

    @Transactional
    public MessageResponse editEmployeeProfile(Integer idEmployee, EditProfileRequest request) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(idEmployee);
            if (!optionalEmployee.isPresent()) {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Employee"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    messageUtil.get("application.status.error")
                );
            }

            Employee employee = optionalEmployee.get();
            String username = employee.getAccount().getUsername();

            if (accountRepository.existsByUsername(request.getUsername()) && !employee.getAccount().getUsername().equals(request.getUsername())) {
                return new MessageResponse(
                    messageUtil.get("application.error.already.exists", "Username"),
                    HttpStatus.BAD_REQUEST.value(),
                    messageUtil.get("application.status.error")
                );
            }

            Optional<Department> optionalDepartment = departmentRepository.findById(request.getIdDepartment());
            if (!optionalDepartment.isPresent()) {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Department"),
                    HttpStatus.BAD_REQUEST.value(),
                    messageUtil.get("application.status.error")
                );
            }

            Department department = optionalDepartment.get();
            Account account = employee.getAccount();
            account.setUsername(request.getUsername());
            employee.setEmail(request.getEmail());
            employee.setFirstName(request.getFirstName());
            employee.setLastName(request.getLastName());
            employee.setDateOfBirth(request.getDateOfBirth());
            employee.setMobileNumber(request.getMobileNumber());
            employee.setGender(request.getGender());
            employee.setMaritalStatus(request.getMaritalStatus());
            employee.setNationality(request.getNationality());
            employee.setAddress(request.getAddress());
            employee.setProvince(request.getProvince());
            employee.setCity(request.getCity());
            employee.setDistrict(request.getDistrict());
            employee.setZipCode(request.getZipCode());
            employee.setEmployeeNumber(request.getEmployeeNumber());
            employee.setStatus(request.getStatus());
            employee.setDepartment(department);
            employee.setRoleCurrentCompany(request.getRoleInCurrentCompany());
            employee.setRoleInClient(request.getRoleInClient());
            employee.setJoiningDate(request.getJoiningDate());
            employee.setModifiedBy(username);
            employee.setModifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))));
            account.setModifiedBy(username);
            account.setModifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))));

            accountRepository.save(account);
            employeeRepository.save(employee);

            EditProfileResponse response = EditProfileResponse.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .dateOfBirth(employee.getDateOfBirth())
                .mobileNumber(employee.getMobileNumber())
                .gender(employee.getGender())
                .maritalStatus(employee.getMaritalStatus())
                .nationality(employee.getNationality())
                .address(employee.getAddress())
                .province(employee.getProvince())
                .city(employee.getCity())
                .district(employee.getDistrict())
                .zipCode(employee.getZipCode())
                .employeeNumber(employee.getEmployeeNumber())
                .username(account.getUsername())
                .status(employee.getStatus())
                .email(employee.getEmail())
                .idDepartment(employee.getDepartment().getIdDepartment())
                .roleInCurrentCompany(employee.getRoleCurrentCompany())
                .roleInClient(employee.getRoleInClient())
                .joiningDate(employee.getJoiningDate())
                .message(messageUtil.get("application.success.update", "Employee profile"))
                .build();

            return new MessageResponse(
                messageUtil.get("application.success.update", "Employee profile"),
                HttpStatus.OK.value(),
                messageUtil.get("application.status.ok"),
                response
            );
        } catch (Exception e) {
            return new MessageResponse(
                messageUtil.get("application.error.internal"),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageUtil.get("application.status.error"),
                e.getMessage()
            );
        }
    }

    @Transactional
    public MessageResponse changeProfilePicture(Integer idEmployee, MultipartFile file) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(idEmployee);
            if (!optionalEmployee.isPresent()) {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Employee"),
                    HttpStatus.NOT_FOUND.value(),
                    messageUtil.get("application.status.error")
                );
            }

            Employee employee = optionalEmployee.get();
            String username = employee.getAccount().getUsername();
            String profilePictureFilename = minioService.uploadFileToMinio(file, "profile_picture_" + employee.getIdEmployee());
            employee.setProfilePicture(profilePictureFilename);
            employee.setModifiedBy(username);
            employee.setModifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))));
            employeeRepository.save(employee);

            return new MessageResponse(
                messageUtil.get("application.success.update", "Profile picture"),
                HttpStatus.OK.value(),
                messageUtil.get("application.status.ok")
            );
        } catch (IOException e) {
            return new MessageResponse(
                messageUtil.get("application.error.upload"),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageUtil.get("application.status.error"),
                e.getMessage()
            );
        }
    }
}
