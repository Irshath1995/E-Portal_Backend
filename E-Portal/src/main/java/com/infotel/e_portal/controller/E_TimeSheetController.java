package com.infotel.e_portal.controller;

import com.infotel.e_portal.service.E_TimeSheetService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/timesheet")
public class E_TimeSheetController {

    private final E_TimeSheetService eTimeSheetService;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message:","No File Selected"));
        }
        try {
            eTimeSheetService.saveAll(file);
            return ResponseEntity.ok(Map.of("message:","File processed and saved successfully."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message:","Something went wrong while processing the file."+e));
        }
    }

    @GetMapping("/weekly-worked-hours")
    public ResponseEntity<?> getWorkedHoursBetweenDates(
            @RequestParam("empId") int empId,
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        try {
            if (fromDate.isAfter(toDate)) {
                return ResponseEntity.badRequest().body("fromDate cannot be after toDate.");
            }

            List<Double> workedHours = eTimeSheetService.getWeeklyWorkedHoursData(empId, fromDate, toDate);

            return ResponseEntity.ok(workedHours);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error fetching worked hours between dates.");
        }
    }
    @GetMapping("/monthly-worked-hours")
    public ResponseEntity<?> getMonthlyWorkedHours(
            @RequestParam("empId") int empId,
            @RequestParam("month") int month,
            @RequestParam("year") int year
    ) {
        try {
            List<Double> monthlyWorkedHours = eTimeSheetService.getMonthlyWorkedHours(empId, month,year);
            return ResponseEntity.ok(monthlyWorkedHours);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message","Error fetching Monthly worked hours data."));
        }
    }


}
