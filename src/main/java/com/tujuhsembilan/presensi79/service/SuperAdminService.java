package com.tujuhsembilan.presensi79.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.tujuhsembilan.presensi79.config.minio.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.AdministratorPatchRequest;
import com.tujuhsembilan.presensi79.dto.request.AdministratorRegistrationRequest;
import com.tujuhsembilan.presensi79.dto.request.CompanyOverviewRequest;
import com.tujuhsembilan.presensi79.dto.request.LoginRequest;
import com.tujuhsembilan.presensi79.dto.request.SuperAdminRegistrationRequest;
import com.tujuhsembilan.presensi79.dto.response.AdministratorRegistrationResponse;
import com.tujuhsembilan.presensi79.dto.response.CompanyResponse;
import com.tujuhsembilan.presensi79.dto.response.DetailAdminProfileResponse;
import com.tujuhsembilan.presensi79.dto.response.SuperadminOverviewDataResponse;
import com.tujuhsembilan.presensi79.dto.response.dataoverview.OverviewAdminResponse;
import com.tujuhsembilan.presensi79.dto.response.dataoverview.OverviewCompanyResponse;
import com.tujuhsembilan.presensi79.dto.response.dataoverview.OverviewEmployeeResponse;
import com.tujuhsembilan.presensi79.dto.response.superadmin.LoginSuperAdminResponse;
import com.tujuhsembilan.presensi79.dto.response.superadmin.SuperAdminRegistrationResponse;
import com.tujuhsembilan.presensi79.exception.CustomErrorWithStatusException;
import com.tujuhsembilan.presensi79.model.Account;
import com.tujuhsembilan.presensi79.model.Admin;
import com.tujuhsembilan.presensi79.model.Attendance;
import com.tujuhsembilan.presensi79.model.Company;
import com.tujuhsembilan.presensi79.model.Employee;
import com.tujuhsembilan.presensi79.model.Superadmin;
import com.tujuhsembilan.presensi79.repository.AccountRepository;
import com.tujuhsembilan.presensi79.repository.AdminRepository;
import com.tujuhsembilan.presensi79.repository.AttendanceRepository;
import com.tujuhsembilan.presensi79.repository.CompanyRepository;
import com.tujuhsembilan.presensi79.repository.EmployeeRepository;
import com.tujuhsembilan.presensi79.repository.SuperadminRepository;
import com.tujuhsembilan.presensi79.security.jwt.JwtUtils;
import com.tujuhsembilan.presensi79.util.MessageUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SuperAdminService {

    private final AdminRepository adminRepository;
    private final AccountRepository accountRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final MessageUtil messageUtil;
    private final Validator validator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final SuperadminRepository superadminRepository;
    private final MinioService minioService;

    // ============ Service for showing Data Overview Dasboard Screen ============//
    public MessageResponse dataOverviewSuperadmin() {
        String message = null;
        HttpStatus status = HttpStatus.OK;
        Object data = null;
        try {
            List<Company> companies = companyRepository.findAll();
            List<Admin> admins = adminRepository.findAll();
            List<Employee> employees = employeeRepository.findAll();
            Timestamp latestCompany = companies.stream()
                    .max(Comparator.comparing(Company::getCreatedDate))
                    .map(Company::getCreatedDate)
                    .orElse(null);
            Timestamp latestAdmin = admins.stream()
                    .max(Comparator.comparing(Admin::getCreatedDate))
                    .map(Admin::getCreatedDate)
                    .orElse(null);
            Timestamp latestEmployee = employees.stream()
                    .max(Comparator.comparing(Employee::getCreatedDate))
                    .map(Employee::getCreatedDate)
                    .orElse(null);
            message = messageUtil.get("application.success.retrieve", "Overview Data");
            Integer countCompanies = companies.size();
            Integer countAdmin = admins.size();
            Integer countEmployee = employees.size();

            data = new SuperadminOverviewDataResponse(
                    new OverviewCompanyResponse(countCompanies, latestCompany),
                    new OverviewAdminResponse(countAdmin, latestAdmin),
                    new OverviewEmployeeResponse(countEmployee, latestEmployee));

        } catch (Exception e) {
            return new MessageResponse(
                    messageUtil.get("application.error.internal"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    messageUtil.get("application.status.error"),
                    e.getMessage());
        }

        return new MessageResponse(message, status.value(), status.getReasonPhrase(), data, null);
    }

    public MessageResponse dataCompanyOverview(CompanyOverviewRequest filter) {
        String message = null;
        HttpStatus status = HttpStatus.OK;
        Object data = null;
        List<Attendance> attendances = null;
        Integer daysCount = 1;
        try {
            List<Employee> employees = employeeRepository.findAll();
            if (filter.getStart_date_filter() == null && filter.getEnd_date_filter() == null) {
                LocalDate nowDate = LocalDate.now();
                attendances = attendanceRepository.findAllAttendanceByDate(nowDate, nowDate);
            } else if (filter.getStart_date_filter() != null && filter.getEnd_date_filter() != null) {
                Long dayLong = filter.getStart_date_filter().until(filter.getEnd_date_filter(), ChronoUnit.DAYS);
                daysCount = (int) (long) dayLong;
                attendances = attendanceRepository.findAllAttendanceByDate(filter.getStart_date_filter(),
                        filter.getEnd_date_filter());
            }

            if (attendances == null) {
                data = new ArrayList<>();
            } else {
                Map<Integer, Integer> companyAttendanceMap = new HashMap<>();

                for (Attendance attendance : attendances) {
                    Employee employee = employees.stream()
                            .filter(e -> e.getIdEmployee().equals(attendance.getEmployee().getIdEmployee()))
                            .findFirst()
                            .orElse(null);
                    if (employee != null) {
                        companyAttendanceMap.merge(employee.getCompany().getIdCompany(), 1, Integer::sum);
                    }
                }

                List<Map<String, Object>> attendanceCounts = new ArrayList<>();
                for (Map.Entry<Integer, Integer> entry : companyAttendanceMap.entrySet()) {
                    Integer companyId = entry.getKey();
                    Integer employeeCount = entry.getValue();
                    Integer attendanceCount = companyAttendanceMap.getOrDefault(companyId, 0);
                    String companyName = employees.stream()
                            .filter(e -> e.getCompany().getIdCompany().equals(companyId))
                            .findFirst()
                            .map(e -> e.getCompany().getCompanyName())
                            .orElse("No Name");

                    Map<String, Object> map = new HashMap<>();
                    map.put("company_name", companyName);
                    map.put("percentage", (double) attendanceCount / (employeeCount * daysCount));
                    attendanceCounts.add(map);
                }
                data = attendanceCounts;
            }
            message = messageUtil.get("application.success.retrieve", "Company Overview");

        } catch (Exception e) {
            return new MessageResponse(
                    messageUtil.get("application.error.internal"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    messageUtil.get("application.status.error"),
                    e.getMessage());
        }

        return new MessageResponse(message, status.value(), status.getReasonPhrase(), data, null);
    }

    public MessageResponse getDetailAdminById(Integer idAdmin) {

        HttpStatus status = HttpStatus.OK;
        Object data = null;
        String message = null;
        String errorMessage = null;
        try {
            Optional<Admin> optionalAdmin = adminRepository.findById(idAdmin);

            if (!optionalAdmin.isPresent()) {
                throw new CustomErrorWithStatusException(messageUtil.get("application.error.notfound", "Admin"),
                        messageUtil.get("application.error.id.notfound", "Admin", idAdmin), HttpStatus.NOT_FOUND);
            }

            Admin admin = optionalAdmin.get();
            Account account = admin.getAccount();  // Mengambil data akun untuk mendapatkan username

            CompanyResponse companyResponse = new CompanyResponse(admin.getCompany().getIdCompany(),
                    admin.getCompany().getCompanyName());

            String profilePictureUrl = null;
            if (admin.getProfilePicture() != null) {
                profilePictureUrl = minioService.getPublicLink(admin.getProfilePicture());
            }

            // Update response untuk menambahkan username
            message = messageUtil.get("application.success.retrieve", "Detail Admin");
            data = new DetailAdminProfileResponse(admin.getIdAdmin(), profilePictureUrl, admin.getFirstName(),
                    admin.getLastName(), admin.getEmail(), account.getUsername(), companyResponse);

        } catch (CustomErrorWithStatusException e) {
            status = e.getStatus();
            message = e.getMessage();
            errorMessage = e.getErrorMessage();
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtil.get("application.error.internal");
            errorMessage = e.getMessage();
        }

        return new MessageResponse(message, status.value(), status.getReasonPhrase(), data, errorMessage);
    }


    @Transactional
    public MessageResponse registerAdministratorTransactional(AdministratorRegistrationRequest request) {

        HttpStatus status = HttpStatus.CREATED;
        Object data = null;
        String message = null;
        String errorMessage = null;
        try {

            Set<ConstraintViolation<AdministratorRegistrationRequest>> constraintViolations = validator
                    .validate(request);

            if (!constraintViolations.isEmpty()) {
                ConstraintViolation<AdministratorRegistrationRequest> firstViolation = constraintViolations.iterator()
                        .next();
                throw new CustomErrorWithStatusException(firstViolation.getMessage(), null,
                        HttpStatus.BAD_REQUEST);
            }

            if (accountRepository.existsByUsername(request.getUsername())) {
                throw new CustomErrorWithStatusException(
                        messageUtil.get("application.error.already.exists", "Username"), null, HttpStatus.BAD_REQUEST);
            }

            Optional<Company> optionalCompany = companyRepository.findById(request.getIdCompany());
            if (!optionalCompany.isPresent()) {
                throw new CustomErrorWithStatusException(
                        messageUtil.get("application.error.notfound", "Company"), null, HttpStatus.BAD_REQUEST);
            }
            Account account = Account.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role("Admin")
                    .createdBy("Super Admin")// to be updated with username of super admin
                    .build();

            account = accountRepository.save(account);
            Admin admin = Admin.builder()
                    .account(account)
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .company(optionalCompany.get())
                    .createdBy("Super Admin")// to be updated with username of super admin
                    .build();

            admin = adminRepository.save(admin);

            CompanyResponse companyResponse = new CompanyResponse(admin.getCompany().getIdCompany(),
                    admin.getCompany().getCompanyName());

            message = messageUtil.get("application.success.create", "Administrator");
            data = new AdministratorRegistrationResponse(admin.getProfilePicture(),
                    admin.getFirstName(),
                    admin.getLastName(), admin.getAccount().getUsername(), admin.getEmail(), companyResponse);

        } catch (CustomErrorWithStatusException e) {
            status = e.getStatus();
            message = e.getMessage();
            errorMessage = e.getErrorMessage();
            throw new CustomErrorWithStatusException(message, errorMessage, status);

        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtil.get("application.error.internal");
            errorMessage = e.getMessage();
            throw new CustomErrorWithStatusException(message, errorMessage, status);
        }

        return new MessageResponse(message, status.value(), status.getReasonPhrase(), data, errorMessage);

    }

    @Transactional
    public MessageResponse patchAdministratorTransactional(Integer idAdmin, AdministratorPatchRequest request,
            MultipartFile profilePicture) {

        HttpStatus status = HttpStatus.OK;
        Object data = null;
        String message = null;
        String errorMessage = null;
        try {
            Set<ConstraintViolation<AdministratorPatchRequest>> constraintViolations = validator.validate(request);

            if (!constraintViolations.isEmpty()) {
                ConstraintViolation<AdministratorPatchRequest> firstViolation = constraintViolations.iterator().next();
                throw new CustomErrorWithStatusException(firstViolation.getMessage(), null, HttpStatus.BAD_REQUEST);
            }

            Optional<Admin> optionalAdmin = adminRepository.findById(idAdmin);

            if (!optionalAdmin.isPresent()) {
                throw new CustomErrorWithStatusException(
                        messageUtil.get("application.error.notfound", "Admin"), null, HttpStatus.BAD_REQUEST);
            }

            Admin tempAdmin = optionalAdmin.get();
            Account tempAccount = tempAdmin.getAccount();

            // Update gambar profil jika ada
            if (profilePicture != null && !profilePicture.isEmpty()) {
                String existingProfilePicture = tempAdmin.getProfilePicture();

                // Jika ada gambar lama, hapus dari Minio
                if (existingProfilePicture != null && !existingProfilePicture.isEmpty()) {
                    try {
                        minioService.deleteFile(existingProfilePicture);
                    } catch (Exception e) {
                        System.out.println("Failed to delete old file from MinIO: " + e.getMessage());
                    }
                }

                // Upload gambar baru dan set nama file baru
                String generatedFilename = minioService.uploadFileToMinio(profilePicture, "admin_profile_" + idAdmin);
                tempAdmin.setProfilePicture(generatedFilename);
            }

            // Update field lain jika ada dalam request
            if (request.getFirstName() != null) {
                tempAdmin.setFirstName(request.getFirstName());
            }

            if (request.getLastName() != null) {
                tempAdmin.setLastName(request.getLastName());
            }

            if (request.getEmail() != null) {
                tempAdmin.setEmail(request.getEmail());
            }

            if (request.getUsername() != null) {
                // Periksa apakah username sudah ada sebelumnya
                if (accountRepository.existsByUsername(request.getUsername())) {
                    throw new CustomErrorWithStatusException(
                            messageUtil.get("application.error.already.exists", "Username"), null, HttpStatus.BAD_REQUEST);
                }

                tempAccount.setUsername(request.getUsername());
            }

            if (request.getIdCompany() != null) {
                Optional<Company> optionalCompany = companyRepository.findById(request.getIdCompany());
                if (!optionalCompany.isPresent()) {
                    throw new CustomErrorWithStatusException(
                            messageUtil.get("application.error.notfound", "Company"), null, HttpStatus.BAD_REQUEST);
                }

                tempAdmin.setCompany(optionalCompany.get());
            }

            // Set nilai modifiedBy ke "Superadmin"
            tempAdmin.setModifiedBy("Superadmin");
            tempAccount.setModifiedBy("Superadmin");

            Admin updatedAdmin = adminRepository.save(tempAdmin);
            accountRepository.save(tempAccount);

            // Persiapkan respons
            CompanyResponse companyResponse = new CompanyResponse(updatedAdmin.getCompany().getIdCompany(),
                    updatedAdmin.getCompany().getCompanyName());

            String profilePictureUrl = null;
            if (updatedAdmin.getProfilePicture() != null) {
                profilePictureUrl = minioService.getPublicLink(updatedAdmin.getProfilePicture());
            }

            message = messageUtil.get("application.success.update", "Administrator");

            data = new DetailAdminProfileResponse(updatedAdmin.getIdAdmin(), profilePictureUrl, updatedAdmin.getFirstName(),
                    updatedAdmin.getLastName(), updatedAdmin.getEmail(), tempAccount.getUsername(), companyResponse);


        } catch (CustomErrorWithStatusException e) {
            status = e.getStatus();
            message = e.getMessage();
            errorMessage = e.getErrorMessage();
            throw new CustomErrorWithStatusException(message, errorMessage, status);

        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtil.get("application.error.internal");
            errorMessage = e.getMessage();
            throw new CustomErrorWithStatusException(message, errorMessage, status);
        }

        return new MessageResponse(message, status.value(), status.getReasonPhrase(), data, errorMessage);
    }

    @Transactional
    public MessageResponse registerSuperAdmin(SuperAdminRegistrationRequest request) {
        try {
            Set<ConstraintViolation<SuperAdminRegistrationRequest>> constraintViolations = validator.validate(request);

            if (!constraintViolations.isEmpty()) {
                ConstraintViolation<SuperAdminRegistrationRequest> firstViolation = constraintViolations.iterator()
                        .next();
                String errorMessage = firstViolation.getMessage();
                return new MessageResponse(errorMessage, HttpStatus.BAD_REQUEST.value(),
                        messageUtil.get("application.status.error"));
            }

            if (accountRepository.existsByUsername(request.getUsername())) {
                return new MessageResponse(
                        messageUtil.get("application.error.already.exists", "Username"),
                        HttpStatus.BAD_REQUEST.value(),
                        messageUtil.get("application.status.error"));
            }

            Account account = Account.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role("Superadmin")
                    .createdBy("Admin")
                    .createdDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))))
                    .modifiedBy("Admin")
                    .modifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))))
                    .build();

            Superadmin superAdmin = Superadmin.builder()
                    .name(request.getName())
                    .createdBy("Admin")
                    .createdDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))))
                    .modifiedBy("Admin")
                    .modifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))))
                    .build();

            Account savedAccount = accountRepository.save(account);
            superAdmin.setAccount(savedAccount);
            Superadmin savedsuperAdmin = superadminRepository.save(superAdmin);

            SuperAdminRegistrationResponse registerSuperAdminResponse = SuperAdminRegistrationResponse.builder()
                    .idAccount(savedAccount.getIdAccount())
                    .idSuperAdmin(savedsuperAdmin.getIdSuperadmin())
                    .name(savedsuperAdmin.getName())
                    .username(savedAccount.getUsername())
                    .role(savedAccount.getRole())
                    .build();
            return new MessageResponse(
                    messageUtil.get("application.success.create", "Super Admin"),
                    HttpStatus.CREATED.value(),
                    messageUtil.get("application.status.ok"),
                    registerSuperAdminResponse);
        } catch (Exception e) {
            return new MessageResponse(
                    messageUtil.get("application.error.internal"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    messageUtil.get("application.status.error"),
                    e.getMessage());
        }
    }

    // ================service to login super admin============================//
    @Transactional
    public MessageResponse loginSuperAdmin(LoginRequest request) {
        try {

            // ---> get account name
            Optional<Account> accountOptional = accountRepository.findByUsername(request.getUsername());

            if (!accountOptional.isPresent()
                    || !passwordEncoder.matches(request.getPassword(), accountOptional.get().getPassword())) {
                return new MessageResponse(
                        messageUtil.get("application.error.login"),
                        HttpStatus.UNAUTHORIZED.value(),
                        messageUtil.get("application.status.unauthorized"));
            }
            Account account = accountOptional.get();

            // ---> get employee id through entity sueradmin
            Optional<Superadmin> superadminOptional = superadminRepository.findByAccount(account);

            if (!superadminOptional.isPresent()) {
                return new MessageResponse(
                        messageUtil.get("application.error.login"),
                        HttpStatus.UNAUTHORIZED.value(),
                        messageUtil.get("application.status.unauthorized"));
            }
            Superadmin superadmin = superadminOptional.get();

            // ---> Buat JWT Token
            Map<String, Object> claims = Map.of(
                    "id_account", account.getIdAccount(),
                    "id_superadmin", superadmin.getIdSuperadmin());

            String token = jwtUtils.generateJwtToken(account.getUsername(), claims);

            LoginSuperAdminResponse loginSuperAdminResponse = LoginSuperAdminResponse.builder()
                    .idAccount(account.getIdAccount())
                    .idSuperAdmin(superadmin.getIdSuperadmin())
                    .token(token)
                    .type("Bearer")
                    .username(account.getUsername())
                    .role(account.getRole())
                    .build();

            return new MessageResponse(
                    messageUtil.get("application.success.login"),
                    HttpStatus.OK.value(),
                    messageUtil.get("application.status.ok"),
                    loginSuperAdminResponse);
        } catch (Exception e) {
            return new MessageResponse(
                    messageUtil.get("application.error.internal"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    messageUtil.get("application.status.error"),
                    e.getMessage());
        }
    }

}

