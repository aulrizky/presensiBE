package com.tujuhsembilan.presensi79.service;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.request.TermsConditionsRequest;
import com.tujuhsembilan.presensi79.dto.response.CompanyConfigResponse;
import com.tujuhsembilan.presensi79.dto.response.TermsConditionsResponse;
import com.tujuhsembilan.presensi79.model.CompanyConfig;
import com.tujuhsembilan.presensi79.repository.CompanyConfigRepository;
import com.tujuhsembilan.presensi79.util.MessageUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyConfigService {

    private final CompanyConfigRepository companyConfigRepository;
    private final MessageUtil messageUtil;

    public MessageResponse getCompanyConfig(Integer idCompany) {
        try {
            CompanyConfig companyConfig = companyConfigRepository.findByCompany_IdCompany(idCompany);
            if (companyConfig != null) {
                CompanyConfigResponse response = CompanyConfigResponse.builder()
                    .idCompanyConfig(companyConfig.getIdCompanyConfig())
                    .idCompany(companyConfig.getCompany().getIdCompany())
                    .workingDayFlexible(companyConfig.getWorkingDayFlexible())
                    .workingDayStart(companyConfig.getWorkingDayStart())
                    .workingDayEnd(companyConfig.getWorkingDayEnd())
                    .workingHoursFlexible(companyConfig.getWorkingHoursFlexible())
                    .workingHoursStart(companyConfig.getWorkingHoursStart())
                    .workingHoursEnd(companyConfig.getWorkingHoursEnd())
                    .workingDurationFlexible(companyConfig.getWorkingDurationFlexible())
                    .workingDuration(companyConfig.getWorkingDuration())
                    .checkInToleranceFlexible(companyConfig.getCheckInToleranceFlexible())
                    .checkInTolerance(companyConfig.getCheckInTolerance())
                    .checkOutToleranceFlexible(companyConfig.getCheckOutToleranceFlexible())
                    .checkOutTolerance(companyConfig.getCheckOutTolerance())
                    .breakTimeFlexible(companyConfig.getBreakTimeFlexible())
                    .breakTime(companyConfig.getBreakTime())
                    .afterBreakToleranceFlexible(companyConfig.getAfterBreakToleranceFlexible())
                    .afterBreakTolerance(companyConfig.getAfterBreakTolerance())
                    .geolocation(companyConfig.getGeolocation())
                    .geolocationRadius(companyConfig.getGeolocationRadius())
                    .selfieMode(companyConfig.getSelfieMode())
                    .autoCheckOut(companyConfig.getAutoCheckOut())
                    .autoCheckOutTime(companyConfig.getAutoCheckOutTime())
                    .createdBy(companyConfig.getCreatedBy())
                    .createdDate(companyConfig.getCreatedDate())
                    .modifiedBy(companyConfig.getModifiedBy())
                    .modifiedDate(companyConfig.getModifiedDate())
                    .build();

                return new MessageResponse(
                    messageUtil.get("application.success.retrieve", "Configuration"),
                    HttpStatus.OK.value(),
                    messageUtil.get("application.status.ok"),
                    response
                );
            } else {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Configuration"),
                    HttpStatus.NOT_FOUND.value(),
                    messageUtil.get("application.status.notfound")
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

    public MessageResponse getTermsConditions(Integer idCompany) {
        try {
            CompanyConfig companyConfig = companyConfigRepository.findByCompany_IdCompany(idCompany);
            if (companyConfig != null) {
                TermsConditionsResponse response = TermsConditionsResponse.builder()
                    .idCompany(companyConfig.getCompany().getIdCompany())
                    .terms_conditions(companyConfig.getTermsConditions())
                    .build();

                return new MessageResponse(
                    messageUtil.get("application.success.retrieve", "Terms and Conditions"),
                    HttpStatus.OK.value(),
                    messageUtil.get("application.status.ok"),
                    response
                );
            } else {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Terms and Conditions"),
                    HttpStatus.NOT_FOUND.value(),
                    messageUtil.get("application.status.notfound")
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

    public MessageResponse createTermsConditions(Integer idCompany, TermsConditionsRequest request) {
        try {
            CompanyConfig companyConfig = companyConfigRepository.findByCompany_IdCompany(idCompany);
            if (companyConfig != null) {
                companyConfig.setTermsConditions(request.getTerms_conditions());
                companyConfig.setCreatedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))));
                companyConfig.setModifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))));
                companyConfigRepository.save(companyConfig);

                return new MessageResponse(
                    messageUtil.get("application.success.create", "Terms and Conditions"),
                    HttpStatus.CREATED.value(),
                    messageUtil.get("application.status.created")
                );
            } else {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Company"),
                    HttpStatus.NOT_FOUND.value(),
                    messageUtil.get("application.status.notfound")
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

    public MessageResponse updateTermsConditions(Integer idCompany, TermsConditionsRequest request) {
        try {
            CompanyConfig companyConfig = companyConfigRepository.findByCompany_IdCompany(idCompany);
            if (companyConfig != null) {
                companyConfig.setTermsConditions(request.getTerms_conditions());
                companyConfig.setModifiedDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Jakarta"))));
                companyConfigRepository.save(companyConfig);

                return new MessageResponse(
                    messageUtil.get("application.success.update", "Terms and Conditions"),
                    HttpStatus.OK.value(),
                    messageUtil.get("application.status.ok")
                );
            } else {
                return new MessageResponse(
                    messageUtil.get("application.error.notfound", "Terms and Conditions"),
                    HttpStatus.NOT_FOUND.value(),
                    messageUtil.get("application.status.notfound")
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
