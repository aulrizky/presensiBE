package com.tujuhsembilan.presensi79.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.tujuhsembilan.presensi79.config.minio.MinioService;
import com.tujuhsembilan.presensi79.dto.request.AddCompanyRequest;
import com.tujuhsembilan.presensi79.dto.request.CompanyPhoto;
import com.tujuhsembilan.presensi79.dto.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.exception.CustomErrorWithStatusException;
import com.tujuhsembilan.presensi79.model.Company;
import com.tujuhsembilan.presensi79.repository.AdminRepository;
import com.tujuhsembilan.presensi79.repository.CompanyRepository;
import com.tujuhsembilan.presensi79.specification.CompanySpecification;
import com.tujuhsembilan.presensi79.util.MessageUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AdminRepository adminRepository;
    private final MessageUtil messageUtil;
    private final MinioService minioService;
    

    public MessageResponse getCompanyLogo(Integer idCompany) {
        try {
            Company company = getCompanyOrThrow(idCompany);
            String logo = company.getCompanyLogo();

            if (logo != null) {
                return new MessageResponse(
                    messageUtil.get("application.success.retrieve", "Company logo"),
                    HttpStatus.OK.value(),
                    messageUtil.get("application.status.ok"),
                    new GetLogoResponse(logo)
                );
            } else {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Company logo"),
                    HttpStatus.NOT_FOUND.value(),
                    messageUtil.get("application.status.notfound"),
                    messageUtil.get("application.error.logo.unavailable")
                );
            }
        } catch (Exception e) {
            return handleInternalError(e);
        }
    }

    public MessageResponse getCompanyCoordinates(Integer idCompany) {
        try {
            Company company = getCompanyOrThrow(idCompany);
            Double latitude = company.getLatitude();
            Double longitude = company.getLongitude();

            if (latitude != null && longitude != null) {
                CompanyCoordinatesResponse response = new CompanyCoordinatesResponse(idCompany, latitude, longitude);
                return new MessageResponse(
                    messageUtil.get("application.success.retrieve", "Company coordinates"),
                    HttpStatus.OK.value(),
                    messageUtil.get("application.status.ok"),
                    response
                );
            } else {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Company coordinates"),
                    HttpStatus.NOT_FOUND.value(),
                    messageUtil.get("application.status.notfound"),
                    messageUtil.get("application.error.coordinates.unavailable")
                );
            }
        } catch (Exception e) {
            return handleInternalError(e);
        }
    }

    private Company getCompanyOrThrow(Integer idCompany) {
        return companyRepository.findById(idCompany)
            .orElseThrow(() -> new IllegalArgumentException(
                messageUtil.get("application.error.invalid", "Company")
            ));
    }

    private MessageResponse handleInternalError(Exception e) {
        return new MessageResponse(
            messageUtil.get("application.error.internal"),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            messageUtil.get("application.status.error"),
            e.getMessage()
        );
    }

    public MessageResponse getCompanies(Pageable page, CompanyFilterRequest request) {

        HttpStatus status = HttpStatus.OK;
        Object data = null;
        String message = null;
        String errorMessage = null;
        try {

            Specification<Company> spec = CompanySpecification.companyFilterAll(request);
            Page<Company> companies = companyRepository.findAll(spec, page);

            if (companies.isEmpty()) {
                throw new CustomErrorWithStatusException(messageUtil.get("application.error.notfound", "Companies"),
                        null, HttpStatus.NOT_FOUND);
            }

            List<CompanyDetailResponse> companiesResponse = companies
                    .stream().map(item -> CompanyDetailResponse.builder()
                            .idCompany(item.getIdCompany())
                            .companyName(item.getCompanyName())
                            .email(item.getEmail())
                            .phone(item.getPhone())
                            .totalAdmin(adminRepository.countByCompanyId(item.getIdCompany()))
                            .createdDate(item.getCreatedDate() != null
                                    ? item.getCreatedDate().toLocalDateTime().toString()
                                    : null)
                            .build())
                    .toList();

            message = messageUtil.get("application.success.retrieve", "Companies");
            data = companiesResponse;

        } catch (CustomErrorWithStatusException e) {
            status = e.getStatus();
            message = e.getMessage();
            errorMessage = e.getErrorMessage();
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = messageUtil.get("application.error.internal");
            errorMessage = e.getMessage();
        }

        return new MessageResponse(message, status.value(), status.getReasonPhrase(),
                data, errorMessage);

    }
    public MessageResponse getCompaniDto(Integer idCompany) {
        try {
            Optional<Company> companyOptional = companyRepository.findById(idCompany);
            if (companyOptional.isPresent()) {
                Company company = companyOptional.get();

                CompanyDto companyDto = CompanyDto.builder()
                        .id_company(company.getIdCompany())
                        .company_name(company.getCompanyName())
                        .company_logo(company.getCompanyLogo())
                        .founder(company.getFounder())
                        .founded_at(company.getFoundedAt())
                        .phone(company.getPhone())
                        .address(company.getAddress())
                        .state(company.getState())
                        .city(company.getCity())
                        .zip_code(company.getZipCode())
                        .joining_date(company.getJoiningDate())
                        .email(company.getEmail())
                        .build();

                return new MessageResponse(
                        "Data found successfully",
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        companyDto
                );
            } else {
                return new MessageResponse(
                        "Company not found",
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "No company with the provided ID"
                );
            }

        } catch (Exception e) {
            return new MessageResponse(
                    "An error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    e.getMessage()
            );
        }
    }

    public MessageResponse postCompany(AddCompanyRequest companyRequest) {
        try {
            // Validasi input request
            if (companyRequest.getCompany_name() == null || companyRequest.getCompany_name().isEmpty()) {
                return new MessageResponse(
                        "error.companyName.required",
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Company name is required"
                );


            }

            // Konversi CompanyRequest menjadi Company Entity
            Company company = Company.builder()
                    .companyName(companyRequest.getCompany_name())
                    .email(companyRequest.getEmail())
                    .phone(companyRequest.getPhone())
                    .address(companyRequest.getAddress())
                    .state(companyRequest.getState())
                    .city(companyRequest.getCity())
                    .zipCode(companyRequest.getZip_code())
                    .joiningDate(companyRequest.getJoining_date())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .createdBy("Super Admin")
                    .modifiedBy("Super Admin")
                    .modifiedDate(new Timestamp(System.currentTimeMillis()))
                    .build();

            // Simpan ke dalam database
            company = companyRepository.save(company);

            // Konversi kembali ke DTO untuk response
            CompanyDto responseDto = CompanyDto.builder()
                    .id_company(company.getIdCompany())  // Mengembalikan ID setelah penyimpanan
                    .company_name(company.getCompanyName())
                    .email(company.getEmail())
                    .phone(company.getPhone())
                    .address(company.getAddress())
                    .state(company.getState())
                    .city(company.getCity())
                    .zip_code(company.getZipCode())
                    .joining_date(company.getJoiningDate())
                    .build();

            return new MessageResponse(
                    "Data found successfully",
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    responseDto
            );

        }catch (Exception e) {
            return new MessageResponse(
                    "An error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    e.getMessage()
            );
        }
    }

    public MessageResponse patchCompanyPhoto(Integer idCompany, MultipartFile companyLogo) {
        try {
            Optional<Company> companyOptional = companyRepository.findById(idCompany);
            if (!companyOptional.isPresent()) {
                return new MessageResponse(
                        "Company not found",
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "No company with the provided ID"
                );
            }

            Company company = companyOptional.get();

            String oldLogo = company.getCompanyLogo();

            // Hapus foto lama
            if(oldLogo != null && !oldLogo.isEmpty()) {
                try {
                    minioService.deleteFile(oldLogo);
                }catch (Exception e) {
                    return new MessageResponse(
                            "Failed to delete old photo from MinIO",
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            e.getMessage()
                    );
                }

            }

            //Upload file baru ke MinIo daan dapatkan nama file yang disimpan di bucket

            String fileName;
            try {
                fileName = minioService.uploadFileToMinio(companyLogo,"company-logo/" + idCompany);
            } catch (IOException e) {
                return new MessageResponse(
                        "Failed to upload photo to MinIO",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        e.getMessage()
                );
            }

            company.setCompanyLogo(fileName);
            companyRepository.save(company);

            // Buat response yang berisi informasi logo perusahaan yang diperbarui
            CompanyPhotoResponse response = CompanyPhotoResponse.builder()
                    .id_company(idCompany)
                    .company_logo(fileName)
                    .build();

            // Kembalikan MessageResponse sukses
            return new MessageResponse(
                    "Company photo updated successfully",
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    response
            );

        } catch (Exception e) {
            return new MessageResponse(
                    "An error occurred while updating the company photo",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    e.getMessage()
            );
        }
    }

    public MessageResponse deleteCompany(Integer idCompany) {
        try {
            Optional<Company> companyOptional = companyRepository.findById(idCompany);
            if (companyOptional.isPresent()) {
                Company company = companyOptional.get();
                
                // Hapus perusahaan dari database
                companyRepository.delete(company);
                
                return new MessageResponse(
                        "Company profile deleted successfully",
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        null
                );
            } else {
                return new MessageResponse(
                        "Company not found",
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "No company with the provided ID"
                );
            }
        } catch (Exception e) {
            return new MessageResponse(
                    "An error occurred while deleting the company profile",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    e.getMessage()
            );
        }
    }
}