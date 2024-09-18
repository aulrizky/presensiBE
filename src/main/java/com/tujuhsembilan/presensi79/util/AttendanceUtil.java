package com.tujuhsembilan.presensi79.util;

import com.tujuhsembilan.presensi79.model.CompanyConfig;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.sql.Timestamp;

public class AttendanceUtil {

    public static String calculateStatus(LocalDateTime checkInTime, CompanyConfig companyConfig) {
        LocalTime checkInLocalTime = checkInTime.toLocalTime();
        LocalTime workingHoursStart = companyConfig.getWorkingHoursStart();

        if (Boolean.TRUE.equals(companyConfig.getWorkingHoursFlexible())) {
            return "On Time";
        } else {
            if (Boolean.TRUE.equals(companyConfig.getCheckInToleranceFlexible())) {
                if (checkInLocalTime.isBefore(workingHoursStart.plusMinutes(10))) {
                    return "On Time";
                }
            } else {
                if (checkInLocalTime.isBefore(workingHoursStart.plusMinutes(companyConfig.getCheckInTolerance()))) {
                    return "On Time";
                }
            }
        }

        return "Late";
    }

    public static boolean isNoteRequired(LocalDateTime checkOutTime, CompanyConfig companyConfig) {
        LocalTime checkOutLocalTime = checkOutTime.toLocalTime();
        LocalTime workingHoursEnd = companyConfig.getWorkingHoursEnd();
        boolean isNoteRequired = false;

        if (Boolean.TRUE.equals(companyConfig.getCheckOutToleranceFlexible())) {
            if (checkOutLocalTime.isBefore(workingHoursEnd.minusMinutes(10))) {
                isNoteRequired = true;
            }
        } else {
            if (companyConfig.getCheckOutTolerance() == null) {
                throw new IllegalArgumentException("Check-out tolerance is missing");
            }
            if (checkOutLocalTime.isBefore(workingHoursEnd.minusMinutes(companyConfig.getCheckOutTolerance()))) {
                isNoteRequired = true;
            }
        }

        return isNoteRequired;
    }

    public static LocalTime calculateTotalWorkingHours(LocalDateTime checkInTime, LocalDateTime checkOutTime, long breakTimeMinutes) {
        long checkInTimeMillis = Timestamp.valueOf(checkInTime).getTime();
        long checkOutTimeMillis = Timestamp.valueOf(checkOutTime).getTime();
        long durationMillis = checkOutTimeMillis - checkInTimeMillis;

        if (durationMillis >= breakTimeMinutes * 60 * 1000) {
            durationMillis -= breakTimeMinutes * 60 * 1000;
        }

        long hours = durationMillis / (1000 * 60 * 60);
        long minutes = (durationMillis / (1000 * 60)) % 60;
        long seconds = (durationMillis / 1000) % 60;

        return LocalTime.of((int) hours, (int) minutes, (int) seconds);
    }
}
