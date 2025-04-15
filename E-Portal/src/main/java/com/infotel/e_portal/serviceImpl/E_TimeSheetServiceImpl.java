package com.infotel.e_portal.serviceImpl;


import com.infotel.e_portal.model.E_TimeSheet;
import com.infotel.e_portal.repository.E_TimeSheetRepository;
import com.infotel.e_portal.service.E_TimeSheetService;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class E_TimeSheetServiceImpl implements E_TimeSheetService {

    private final E_TimeSheetRepository eTimeSheetRepository;

    @Override
    public void saveAll(MultipartFile file) {
        List<E_TimeSheet> timeSheetList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i <= sheet.getLastRowNum(); i++) { // Skip every alternate row
                Row row = sheet.getRow(i);
                if (row == null) continue;
                try {
                    if (row.getCell(1) == null || row.getCell(2) == null || row.getCell(3) == null||
                            (row.getCell(5) != null && row.getCell(5).getStringCellValue().equals("5 : FRONT ENTRANCE")))
                        continue;

                    Integer userId =Integer.parseInt(row.getCell(1).getStringCellValue());
                    String name = row.getCell(2).getStringCellValue();

                    // Time
                    String punchDate="";
                    String  punchTime="";
                    Cell timeCell = row.getCell(3);
                    if (DateUtil.isCellDateFormatted(timeCell)) {
                        punchDate = dateFormat.format(timeCell.getDateCellValue());
                        punchTime = timeFormat.format(timeCell.getDateCellValue());
                    }
                    String type = row.getCell(4).getStringCellValue();
                    String status = row.getCell(8).getStringCellValue();
                    LocalDate localDate=LocalDate.parse(punchDate);
                    LocalTime localTime=LocalTime.parse(punchTime);
                    boolean inOutType = type.equalsIgnoreCase("IN");
                    boolean eventStatus = status.equalsIgnoreCase("ALLOWED");


                    E_TimeSheet record = new E_TimeSheet(userId, localDate, localTime, inOutType, eventStatus);
                    timeSheetList.add(record);

                } catch (Exception e) {
                    System.out.println("Skipping row " + i + " due to: " + e.getMessage());
                }
            }

            workbook.close();
            timeSheetList.forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException("Error processing the Excel file: " + e.getMessage(), e);
        }
        eTimeSheetRepository.saveAll(timeSheetList);
    }

    @Override
    public Map<LocalDate, Map<String, Object>> getFirstInAndLastOutForEachDay(int empId, LocalDate selectedDate) {
        List<E_TimeSheet> employeeTimesheet = eTimeSheetRepository.findByEmpIdAndWeek(empId, selectedDate);
        Map<LocalDate, Map<String, Object>> map = new HashMap<>();
        List<E_TimeSheet> sortedTimesheet = employeeTimesheet.stream()
                .sorted(Comparator.comparing(E_TimeSheet::getPunchDate)).collect(Collectors.toList());
        List<E_TimeSheet> filteredTimesheet = sortedTimesheet.stream().filter(timesheet -> timesheet.isEventStatus())
                .collect(Collectors.toList());
        filteredTimesheet.sort(Comparator.comparing(E_TimeSheet::getPunchDate));

        LocalDate currentDate = null;
        Boolean currentInouttype = null;
        LocalTime currentPunchTime = null;
        List<Map<String, Object>> successivePairs = new ArrayList<>();
        Map<LocalDate, Duration> dailyTotalDurations = new HashMap<>();
        for (E_TimeSheet timesheet : filteredTimesheet) {
            LocalDate punchDate = timesheet.getPunchDate();
            Boolean inouttype = timesheet.isInOutType();
            LocalTime punchTime = timesheet.getPunchTime();
            if (!punchDate.equals(currentDate)) {
                currentInouttype = null;
                currentPunchTime = null;
            }
            if (currentInouttype != null && currentInouttype != inouttype) {
                if (inouttype == false && currentInouttype == true) {
                    Duration duration = Duration.between(currentPunchTime, punchTime);
                    Map<String, Object> pair = Map.of("date", punchDate, "inTime", currentPunchTime, "outTime",
                            punchTime, "duration", duration);
                    successivePairs.add(pair);
                    dailyTotalDurations.merge(punchDate, duration, Duration::plus);
                }
            }
            currentDate = punchDate;
            currentInouttype = inouttype;
            currentPunchTime = punchTime;
        }
        Duration duration;
        for (Map<String, Object> pair : successivePairs) {
            LocalDate date = (LocalDate) pair.get("date");
            LocalTime inTime = (LocalTime) pair.get("inTime");
            duration = (Duration) pair.get("duration");
        }
        Duration total = Duration.ZERO;
        for (Map.Entry<LocalDate, Duration> entry : dailyTotalDurations.entrySet()) {
            LocalDate date = entry.getKey();
            Duration totalDuration = entry.getValue();
            total = total.plus(totalDuration);
        }

        LocalTime firstInTime = null;
        LocalTime lastOutTime = null;
        String formattedTotalHours = null;
        String formattedBreakHours = null;
        for (Map.Entry<LocalDate, Duration> entry : dailyTotalDurations.entrySet()) {
            LocalDate date = entry.getKey();
            Duration totalDuration = entry.getValue();
            firstInTime = null;
            lastOutTime = null;
            for (Map<String, Object> pair : successivePairs) {
                LocalDate pairDate = (LocalDate) pair.get("date");
                LocalTime inTime = (LocalTime) pair.get("inTime");
                LocalTime outTime = (LocalTime) pair.get("outTime");
                if (pairDate.equals(date)) {
                    if (firstInTime == null || inTime.isBefore(firstInTime)) {
                        firstInTime = inTime;
                    }
                    if (lastOutTime == null || outTime.isAfter(lastOutTime)) {
                        lastOutTime = outTime;
                    }
                }
            }
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            List<Double> totalHoursList = new ArrayList<>();
            totalHoursList.add((double) totalDuration.toHours());
            totalHoursList.add((double) totalDuration.toMinutes() % 60);
            totalHoursList.add((double) totalDuration.toSeconds() % 60);
            double hours = totalHoursList.get(0);
            double minutes = totalHoursList.get(1);
            double seconds = totalHoursList.get(2);
            if (hours == 0.0) {
                formattedTotalHours = String.format("%.0fm", minutes);
            } else {
                formattedTotalHours = String.format("%.0fh %.0fm", hours, minutes);
            }
            Duration breakHours = Duration.ofHours(8).minus(totalDuration);
            if (breakHours.isNegative()) {
                formattedBreakHours = null;
            } else {
                List<Double> totalHoursList1 = new ArrayList<>();
                totalHoursList1.add((double) breakHours.toHours());
                totalHoursList1.add((double) breakHours.toMinutes() % 60);
                totalHoursList1.add((double) breakHours.toSeconds() % 60);
                double hours1 = totalHoursList1.get(0);
                double minutes1 = totalHoursList1.get(1);
                double seconds1 = totalHoursList1.get(2);
                if (hours1 == 0.0) {
                    formattedBreakHours = String.format("%.0fm", minutes1);
                } else {
                    formattedBreakHours = String.format("%.0fh %.0fm", hours1, minutes1, seconds1);
                }
            }

            Map<String, Object> resultArray = new HashMap<>();
            resultArray.put("punchIn", firstInTime);
            resultArray.put("punchOut", lastOutTime);
            resultArray.put("totalBreakEachDay", formattedBreakHours);
            resultArray.put("totalWorkedEachDay", formattedTotalHours);
            map.put(date, resultArray);
        }

        return map;
    }

    @Override
    public List<Double> getWeeklyWorkedHoursData(int empId, LocalDate fromDate,LocalDate toDate) {
        List<E_TimeSheet> employeeTimesheet = eTimeSheetRepository.findByEmpIdAndDateRange(empId, fromDate,toDate);
        if (employeeTimesheet.isEmpty()) {
            List<Double> dblList = new ArrayList<>();
            dblList.add(0.0);
            dblList.add(0.0);
            dblList.add(0.0);
            dblList.add(0.0);
            return dblList;
        }
        List<E_TimeSheet> sortedTimesheet = employeeTimesheet.stream()
                .sorted(Comparator.comparing(E_TimeSheet::getPunchDate)).collect(Collectors.toList());

        /* getting the records which have only event status as true or 1 */
        List<E_TimeSheet> filteredTimesheet = sortedTimesheet.stream().filter(timesheet -> timesheet.isEventStatus())
                .collect(Collectors.toList());

        /*
         * Sort the filtered records according to date and time
         */

        filteredTimesheet.sort(Comparator.comparing(E_TimeSheet::getPunchDate));

        LocalDate currentDate = null;
        Boolean currentInouttype = null;
        LocalTime currentPunchTime = null;
        List<Map<String, Object>> successivePairs = new ArrayList<>();
        Map<LocalDate, Duration> dailyTotalDurations = new HashMap<>();
        for (E_TimeSheet timesheet : filteredTimesheet) {
            LocalDate punchDate = timesheet.getPunchDate();
            Boolean inouttype = timesheet.isInOutType();
            LocalTime punchTime = timesheet.getPunchTime();
            if (!punchDate.equals(currentDate)) {
                // Reset when a new date is encountered
                currentInouttype = null;
                currentPunchTime = null;
            }
            if (currentInouttype != null && currentInouttype != inouttype) {
                if (inouttype == false && currentInouttype == true) {
                    // Calculate duration for IN-OUT pair
                    Duration duration = Duration.between(currentPunchTime, punchTime);
                    Map<String, Object> pair = Map.of("date", punchDate, "inTime", currentPunchTime, "outTime",
                            punchTime, "duration", duration);
                    successivePairs.add(pair);
                    // Update daily total duration
                    dailyTotalDurations.merge(punchDate, duration, Duration::plus);
                }
            }
            currentDate = punchDate;
            currentInouttype = inouttype;
            currentPunchTime = punchTime;
        }
        Duration duration;
        for (Map<String, Object> pair : successivePairs) {
            LocalDate date = (LocalDate) pair.get("date");
            LocalTime inTime = (LocalTime) pair.get("inTime");
            // LocalTime outTime = (LocalTime) pair.get("outTime");
            duration = (Duration) pair.get("duration");

        }
        Duration total = Duration.ZERO;
        // Print daily total durations
        for (Map.Entry<LocalDate, Duration> entry : dailyTotalDurations.entrySet()) {
            LocalDate date = entry.getKey();
            Duration totalDuration = entry.getValue();

            // Add the daily duration to the total duration
            total = total.plus(totalDuration);
        }

        // Convert totalDuration to a list of Double (hours)
        List<Double> totalHoursList = new ArrayList<>();
        totalHoursList.add((double) total.toHours());
        totalHoursList.add((double) total.toMinutes() % 60);
        totalHoursList.add((double) total.toSeconds() % 60);

        double hours = totalHoursList.get(0);
        double minutes = totalHoursList.get(1);
        double seconds = totalHoursList.get(2);
        String formattedTotalHours = String.format("%.0f.%.0f.%.0f", hours, minutes, seconds);

        // String formattedTotalHours ="50.00.00";

        // Split the formatted string into hours, minutes, and seconds
        String[] parts = formattedTotalHours.split("\\.");
        double hours1 = Double.parseDouble(parts[0]);
        double minutes1 = Double.parseDouble(parts[1]);
        double seconds1 = Double.parseDouble(parts[2]);
        // Calculate the total duration in seconds
        double totalSeconds = (hours1 * 3600) + (minutes1 * 60) + seconds1;
        // Calculate the total break hours
        double actualWeeklyHours = 40.0; // 8 hours per day * 5 days = 40 hours
        double workedWeeklyHours = totalSeconds / 3600.0;
        double totalBreakHoursForWeek = actualWeeklyHours - workedWeeklyHours;

        if (totalBreakHoursForWeek <= 0) {
            totalBreakHoursForWeek = 0;
        }

        List<Double> totalHoursList1 = new ArrayList<>();
        totalHoursList1.add(workedWeeklyHours);
        totalHoursList1.add(totalBreakHoursForWeek);
        return totalHoursList1;
    }
    @Override
    public List<Double> getMonthlyWorkedHours(int empId, int month, int year) throws IOException, ParseException {
        List<E_TimeSheet> results = eTimeSheetRepository.findByEmpIdAndMonth(empId, month, year);
        if (results.isEmpty()) {
            List<Double> dblList = new ArrayList<>();
            dblList.add(0.0);
            dblList.add(0.0);
            dblList.add(0.0);
            dblList.add(0.0);
            return dblList;
        }
        List<Double> totalHoursList1 = new ArrayList<>();
        if (!results.isEmpty()) {
            List<E_TimeSheet> sortedTimesheet = results.stream().sorted(Comparator.comparing(E_TimeSheet::getPunchDate))
                    .toList();
            List<E_TimeSheet> filteredTimesheet = sortedTimesheet.stream()
                    .filter(timesheet -> timesheet.isEventStatus()).collect(Collectors.toList());
            // Sort the filtered records

            filteredTimesheet.sort(Comparator.comparing(E_TimeSheet::getPunchDate));

            Map<LocalDate, List<E_TimeSheet>> timesheetByDate = sortedTimesheet.stream()
                    .collect(Collectors.groupingBy(E_TimeSheet::getPunchDate));
            List<Map<String, Object>> successivePairs = new ArrayList<>();
            Map<LocalDate, Duration> dailyTotalDurations = new HashMap<>();
            LocalDate currentDate = null;
            Boolean currentInouttype = null;
            LocalTime currentPunchTime = null;
            for (E_TimeSheet timesheet : filteredTimesheet) {
                LocalDate punchDate = timesheet.getPunchDate();
                Boolean inouttype = timesheet.isInOutType();
                LocalTime punchTime = timesheet.getPunchTime();
                if (!punchDate.equals(currentDate)) {
                    currentInouttype = null;
                    currentPunchTime = null;
                }
                if (currentInouttype != null && currentInouttype != inouttype) {
                    if (inouttype == false && currentInouttype == true) {
                        Duration duration = Duration.between(currentPunchTime, punchTime);
                        Map<String, Object> pair = Map.of("date", punchDate, "inTime", currentPunchTime, "outTime",
                                punchTime, "duration", duration);
                        successivePairs.add(pair);
                        dailyTotalDurations.merge(punchDate, duration, Duration::plus);
                    }
                }
                currentDate = punchDate;
                currentInouttype = inouttype;
                currentPunchTime = punchTime;
            }
            LocalDate startDate = LocalDate.of(year, month, 1);       // Start date of the month
            LocalDate endDate = startDate.plusMonths(1).minusDays(1); // End date of the month
            int workingDays = 0;
            for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {
                if (isBusinessDay(date)) {
                    workingDays++;
                }
            }
            int tMHforBussinessDays = workingDays * 8; // 8 hours per day
            double totalHours = Double.valueOf(tMHforBussinessDays);
            Duration total = Duration.ZERO;
            for (Map<String, Object> pair : successivePairs) {
                LocalDate date = (LocalDate) pair.get("date");
                LocalTime inTime = (LocalTime) pair.get("inTime");
                LocalTime outTime = (LocalTime) pair.get("outTime");
                Duration duration = (Duration) pair.get("duration");
            }

            for (Map.Entry<LocalDate, Duration> entry : dailyTotalDurations.entrySet()) {
                LocalDate date = entry.getKey();
                Duration totalDuration = entry.getValue();
                total = total.plus(totalDuration);
            }

            List<Double> totalHoursList = new ArrayList<>();
            totalHoursList.add((double) total.toHours());
            totalHoursList.add((double) total.toMinutes() % 60);
            totalHoursList.add((double) total.toSeconds() % 60);

            double hours = totalHoursList.get(0);
            double minutes = totalHoursList.get(1);
            double seconds = totalHoursList.get(2);
            String formattedTotalHours = String.format("%.0f.%.0f.%.0f", hours, minutes, seconds);

            // Split the formatted string into hours, minutes, and seconds
            String[] parts = formattedTotalHours.split("\\.");
            double hours1 = Double.parseDouble(parts[0]);
            double minutes1 = Double.parseDouble(parts[1]);
            double seconds1 = Double.parseDouble(parts[2]);
            // Calculate the total duration in seconds
            double totalSeconds = (hours1 * 3600) + (minutes1 * 60) + seconds1;
            // Calculate the total break hours
            double actualWeeklyHours = totalHours; // 8 hours * 5 days
            double workedWeeklyHours = totalSeconds / 3600.0;
            double totalBreakHoursForWeek = actualWeeklyHours - workedWeeklyHours;
            if (totalBreakHoursForWeek <= 0) {
                totalBreakHoursForWeek = 0;
            }

            totalHoursList1.add(workedWeeklyHours);
            totalHoursList1.add(totalBreakHoursForWeek);
        }
        return totalHoursList1;
    }
    @Override
    public boolean isBusinessDay(LocalDate date) {
        return date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY;
    }
}
