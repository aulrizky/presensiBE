package com.tujuhsembilan.presensi79.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tujuhsembilan.presensi79.config.minio.MinioService;
import com.tujuhsembilan.presensi79.dto.response.AdminPhotoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.AdminManagementRequest;
import com.tujuhsembilan.presensi79.dto.request.AdminPhotoRequest;
import com.tujuhsembilan.presensi79.dto.response.AdminManagementResponse;
import com.tujuhsembilan.presensi79.dto.response.CompanyResponse;
import com.tujuhsembilan.presensi79.model.Admin;
import com.tujuhsembilan.presensi79.model.Company;
import com.tujuhsembilan.presensi79.repository.AdminRepository;
import com.tujuhsembilan.presensi79.repository.CompanyRepository;
import com.tujuhsembilan.presensi79.specification.AdminManagementSpecification;
import com.tujuhsembilan.presensi79.util.MessageUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminManagementService {

  final private AdminRepository adminRepository;
  final CompanyRepository companyRepository;
  final MessageUtil messageUtil;

  @Autowired
  private MinioService minioService; // Inject MinioService

  @Transactional
    public MessageResponse getAdminList(
        int pageNumber,
        int pageSize,
        AdminManagementRequest filter) {
        
        try {
            if (pageNumber < 1) {
                pageNumber = 1;
            }

            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, 
                Sort.by(Sort.Direction.ASC, "company.companyName").and(Sort.by("firstName").ascending()).and(Sort.by("lastName").ascending()));

            Specification<Admin> specification = AdminManagementSpecification.administratorFilter(filter)
                .and((root, query, builder) -> builder.isFalse(root.get("isDeleted")));

            List<Admin> admins = adminRepository.findAll(specification, pageable).getContent();

            List<AdminManagementResponse> adminResponses = admins.stream()
                .map(admin -> AdminManagementResponse.builder()
                    .idAdmin(admin.getIdAdmin())
                    .company(new CompanyResponse(
                        admin.getCompany().getIdCompany(),
                        admin.getCompany().getCompanyName()))
                    .profilePicture(admin.getProfilePicture())
                    .firstName(admin.getFirstName())
                    .lastName(admin.getLastName())
                    .email(admin.getEmail())
                    .createdDate(admin.getCreatedDate())
                    .build())
                .collect(Collectors.toList());

            return new MessageResponse(
                messageUtil.get("application.success.retrieve", "Admins"),
                HttpStatus.OK.value(),
                messageUtil.get("application.status.ok"),
                adminResponses);

        } catch (Exception e) {
            e.printStackTrace();
            return new MessageResponse(
                messageUtil.get("application.error.internal"),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageUtil.get("application.status.error"),
                e.getMessage());
        }
    }

  public MessageResponse patchAdminPhto(Integer idAdmin, MultipartFile profilePicture) {
    try {
      Optional<Admin> optionalAdmin = adminRepository.findById(idAdmin);
      if (!optionalAdmin.isPresent()) {
        return new MessageResponse(
                "Admin not found",
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "No admin with the provided ID"
        );
      }

      Admin admin = optionalAdmin.get();

      // Ambil nama file foto lama
      String oldProfilePicture = admin.getProfilePicture();

      // Hapus foto lama dari MinIO
      if (oldProfilePicture != null && !oldProfilePicture.isEmpty()) {
        try {
          minioService.deleteFile(oldProfilePicture);
        } catch (Exception e) {
          return new MessageResponse(
                  "Failed to delete old photo from MinIO",
                  HttpStatus.INTERNAL_SERVER_ERROR.value(),
                  HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                  e.getMessage()
          );
        }
      }

      // Upload file baru ke MinIO dan dapatkan nama file yang disimpan di bucket
      String fileName;
      try {
        fileName = minioService.uploadFileToMinio(profilePicture, "admin-photos/" + idAdmin);
      } catch (IOException e) {
        return new MessageResponse(
                "Failed to upload photo to MinIO",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                e.getMessage()
        );
      }

      // Simpan path atau URL file baru ke database
      admin.setProfilePicture(fileName);
      adminRepository.save(admin);

      // Buat response yang berisi informasi foto admin yang diperbarui
      AdminPhotoResponse adminResponse = AdminPhotoResponse.builder()
              .id_admin(idAdmin)
              .profile_picture(fileName) // File name yang disimpan di MinIO
              .build();

      // Kembalikan MessageResponse sukses
      return new MessageResponse(
              "Admin photo updated successfully",
              HttpStatus.OK.value(),
              HttpStatus.OK.getReasonPhrase(),
              adminResponse
      );

    } catch (Exception e) {
      return new MessageResponse(
              "An error occurred while updating the admin photo",
              HttpStatus.INTERNAL_SERVER_ERROR.value(),
              HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
              e.getMessage()
      );
    }
  }

  @Transactional
    public MessageResponse getCompanyList() {
        try {
            List<Company> companies = companyRepository.findAll();

            List<CompanyResponse> companyResponses = companies.stream()
                .map(company -> CompanyResponse.builder()
                    .idCompany(company.getIdCompany())
                    .companyName(company.getCompanyName())
                    .build())
                .collect(Collectors.toList());

            return new MessageResponse(
                messageUtil.get("application.success.retrieve", "Companies"),
                HttpStatus.OK.value(),
                "OK",
                companyResponses
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new MessageResponse(
                messageUtil.get("application.error.internal"),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                e.getMessage()
            );
        }
    }
}

