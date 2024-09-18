package com.tujuhsembilan.presensi79.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.dto.response.HolidayResponse;
import com.tujuhsembilan.presensi79.model.CompanyConfig;
import com.tujuhsembilan.presensi79.model.Holiday;
import com.tujuhsembilan.presensi79.repository.CompanyConfigRepository;
import com.tujuhsembilan.presensi79.repository.HolidayRepository;
import com.tujuhsembilan.presensi79.util.MessageUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayService {

    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);

    private final HolidayRepository holidayRepository;
    private final CompanyConfigRepository companyConfigRepository;
    private final NagerDateService nagerDateService;
    private final MessageUtil messageUtil;

    public MessageResponse getHolidays(int year, Integer companyId, int page, int size) {
        try {
            // Ensure page is not less than 1
            if (page < 1) {
                page = 1;
            }

            CompanyConfig companyConfig = companyConfigRepository.findByCompany_IdCompany(companyId);
            List<HolidayResponse> holidays;
            int totalElements = 0;
            int totalPages = 0;

            if (companyConfig.getDefaultHoliday()) {
                holidays = nagerDateService.getPublicHolidays(year, "ID").stream()
                        .map(holiday -> HolidayResponse.builder()
                                .holidayName(holiday.getName())
                                .date(holiday.getDate())
                                .build())
                        .collect(Collectors.toList());

                // Logging jumlah total data yang diambil dari nagerDateService
                logger.info("Total holidays from nagerDateService: {}", holidays.size());
                
                totalElements = holidays.size();
                totalPages = (int) Math.ceil((double) totalElements / size);
                
                // Manual pagination
                int start = Math.min((page - 1) * size, holidays.size());
                int end = Math.min(start + size, holidays.size());
                holidays = holidays.subList(start, end);
                
                logger.info("Default holidays pagination: start={}, end={}, totalElements={}, totalPages={}", start, end, totalElements, totalPages);
            } else {
                Page<Holiday> holidayPage = holidayRepository.findByCompany_IdCompany(companyConfig.getCompany().getIdCompany(), PageRequest.of(page - 1, size));
                totalElements = (int) holidayPage.getTotalElements();
                totalPages = holidayPage.getTotalPages();
                holidays = holidayPage.stream()
                        .map(holiday -> HolidayResponse.builder()
                                .idHoliday(holiday.getIdHoliday())
                                .holidayName(holiday.getHolidayName())
                                .date(holiday.getDate())
                                .isDeleted(holiday.getIsDeleted())
                                .build())
                        .collect(Collectors.toList());
                
                logger.info("Custom holidays pagination: page={}, size={}, totalElements={}, totalPages={}", page, size, totalElements, totalPages);
            }

            if (!holidays.isEmpty()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("holidays", holidays);
                responseData.put("totalElements", totalElements);
                responseData.put("totalPages", totalPages);

                return new MessageResponse(
                        messageUtil.get("application.success.retrieve", "Holidays"),
                        HttpStatus.OK.value(),
                        messageUtil.get("application.status.ok"),
                        responseData
                );
            } else {
                return new MessageResponse(
                        messageUtil.get("application.error.notfound", "Holidays"),
                        HttpStatus.NOT_FOUND.value(),
                        messageUtil.get("application.status.notfound")
                );
            }
        } catch (Exception e) {
            logger.error("Error retrieving holidays", e);
            return new MessageResponse(
                    messageUtil.get("application.error.internal"),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    messageUtil.get("application.status.error"),
                    e.getMessage()
            );
        }
    }
}
