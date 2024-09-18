package com.tujuhsembilan.presensi79.util;

public class Messages {

    // Success Messages
    public static final String SUCCESS_RETRIEVE_ATTENDANCE_DETAILS = "Successfully retrieved attendance details";
    public static final String SUCCESS_CREATE_ATTENDANCE = "Attendance created successfully";
    public static final String SUCCESS_CHECKIN = "Check-in successful";
    public static final String SUCCESS_CHECKOUT = "Check-out successful";
    public static final String SUCCESS_RETRIEVE_DAYS_WORKED = "Days worked retrieved successfully";
    public static final String SUCCESS_RETRIEVE_CONFIG = "Successfully retrieved configuration";
    public static final String SUCCESS_UPDATE_CONFIG = "Configuration updated successfully";
    public static final String SUCCESS_CREATE_CONFIG = "Configuration created successfully";

    // Error Messages
    public static final String ERROR_NOT_FOUND_ATTENDANCE_DETAILS = "Attendance details not found";
    public static final String ERROR_NOT_FOUND_ATTENDANCE_RECORDS = "Attendance records for the current month not found";
    public static final String ERROR_INTERNAL = "Internal server error";
    public static final String ERROR_INVALID_DATA = "Invalid data";
    public static final String ERROR_NOT_FOUND_CONFIG = "Configuration not found";

    // Tag Descriptions
    public static final String TAG_ATTENDANCE_DESCRIPTION = "Employee Attendance Management APIs";
    public static final String TAG_ACCOUNT_DESCRIPTION = "Account Management APIs";
    public static final String TAG_ADMIN_DESCRIPTION = "Admin Management APIs";
    public static final String TAG_COMPANY_CONFIG_DESCRIPTION = "Company Configuration Management APIs";
    public static final String TAG_COMPANY_DESCRIPTION = "Company Management APIs";
    public static final String TAG_DEPARTMENT_DESCRIPTION = "Department Management APIs";
    public static final String TAG_EMPLOYEE_DESCRIPTION = "Employee Management APIs";
    public static final String TAG_HOLIDAY_DESCRIPTION = "Company's Holiday Management APIs";
    public static final String TAG_SUPER_ADMIN_DESCRIPTION = "Super Admin Management APIs";
    public static final String TAG_ADMIN_LIST_DESCRIPTION = "Fetch All Data Admin Management APIs";
    // Operation Descriptions
    public static final String OPERATION_GET_ATTENDANCE_DETAILS = "Fetch the attendance details of the authenticated employee";
    public static final String OPERATION_CHECKIN_ATTENDANCE = "Create a new attendance entry for an employee";
    public static final String OPERATION_CHECKOUT_ATTENDANCE = "Update the attendance entry for an employee";
    public static final String OPERATION_AUTOCHECKOUT_ATTENDANCE = "Automatically checks out an employee's attendance.";
    public static final String OPERATION_GET_DAYS_WORKED = "Fetch the total days worked by the authenticated employee for the current month";
    public static final String OPERATION_GET_COMPANY_CONFIG = "Fetch the company configuration for the authenticated company";
    public static final String OPERATION_GET_TERMS_CONDITIONS = "Fetch the terms and conditions for the authenticated company";
    public static final String OPERATION_UPDATE_TERMS_CONDITIONS = "Update the terms and conditions for the authenticated company";
    public static final String OPERATION_CREATE_TERMS_CONDITIONS = "Create new terms and conditions for the authenticated company";
}
