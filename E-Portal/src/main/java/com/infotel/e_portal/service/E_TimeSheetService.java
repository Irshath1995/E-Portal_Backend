package com.infotel.e_portal.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface E_TimeSheetService {

    void saveAll(MultipartFile file);

    public Map<LocalDate, Map<String, Object>> getFirstInAndLastOutForEachDay(int empId, LocalDate selectedDate);
    List<Double> getWeeklyWorkedHoursData(int empId, LocalDate fromDate,LocalDate toDate);
    List<Double> getMonthlyWorkedHours(int empId, int month, int year) throws IOException, ParseException;
    boolean isBusinessDay(LocalDate date);
}
