package com.tujuhsembilan.presensi79.scheduler;

import com.tujuhsembilan.presensi79.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoCheckOutScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AutoCheckOutScheduler.class);
    private final AttendanceService attendanceService;

    @Scheduled(cron = "*/10 * * * * *", zone = "Asia/Jakarta") // This cron expression runs every 10 seconds
    public void autoCheckOut() {
        logger.info("Auto check-out scheduler triggered at");
        attendanceService.autoCheckOutEmployees();
    }
}
