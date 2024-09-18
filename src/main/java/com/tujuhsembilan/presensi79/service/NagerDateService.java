package com.tujuhsembilan.presensi79.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import com.tujuhsembilan.presensi79.dto.PublicHolidayDTO;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NagerDateService {

    private final RestTemplate restTemplate;
    private final Gson gson;

    private static final String API_URL = "https://date.nager.at/api/v3/publicholidays/{year}/{countryCode}";

    public List<PublicHolidayDTO> getPublicHolidays(int year, String countryCode) {
        try {
            String json = restTemplate.getForObject(API_URL, String.class, year, countryCode);
            PublicHolidayDTO[] response = gson.fromJson(json, PublicHolidayDTO[].class);
            return Arrays.asList(response);
        } catch (Exception e) {
            System.err.println("HTTP Error: " + e.getMessage());
            throw e;
        }
    }
}
