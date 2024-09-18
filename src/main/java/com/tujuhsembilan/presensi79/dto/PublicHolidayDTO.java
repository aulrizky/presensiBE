package com.tujuhsembilan.presensi79.dto;

import java.time.LocalDate;

public class PublicHolidayDTO {

    private LocalDate date;
    private String localName;
    private String name;
    private String countryCode;
    private Boolean fixed;
    private Boolean global;
    private String counties;
    private Integer launchYear;
    private String type;
    
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getLocalName() {
        return localName;
    }
    public void setLocalName(String localName) {
        this.localName = localName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public Boolean getFixed() {
        return fixed;
    }
    public void setFixed(Boolean fixed) {
        this.fixed = fixed;
    }
    public Boolean getGlobal() {
        return global;
    }
    public void setGlobal(Boolean global) {
        this.global = global;
    }
    public String getCounties() {
        return counties;
    }
    public void setCounties(String counties) {
        this.counties = counties;
    }
    public Integer getLaunchYear() {
        return launchYear;
    }
    public void setLaunchYear(Integer launchYear) {
        this.launchYear = launchYear;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
