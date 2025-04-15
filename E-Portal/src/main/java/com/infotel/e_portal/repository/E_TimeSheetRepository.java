package com.infotel.e_portal.repository;

import com.infotel.e_portal.model.E_TimeSheet;
import com.infotel.e_portal.model.E_TimeSheetIdentiy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface E_TimeSheetRepository extends JpaRepository<E_TimeSheet, E_TimeSheetIdentiy> {

    @Query("SELECT e FROM E_TimeSheet e WHERE e.empId = :empId AND e.punchDate = :punchDate")
    List<E_TimeSheet> findByEmpIdAndPunchDate(@Param("empId") int empId, @Param("punchDate") LocalDate punchDate);

    @Query(value = "SELECT * FROM timesheet_raw e " + "WHERE e.user_id = :empId "
            + "AND e.punch_date >= DATE_SUB(:selectedDate, INTERVAL DAYOFWEEK(:selectedDate) - 1 DAY) "
            + "AND e.punch_date < DATE_ADD(:selectedDate, INTERVAL 7 - DAYOFWEEK(:selectedDate) DAY)", nativeQuery = true)
    List<E_TimeSheet> findByEmpIdAndWeek(@Param("empId") int empId, @Param("selectedDate") LocalDate selectedDate);

    @Query(value = "SELECT * FROM timesheet_raw e " +
            "WHERE e.user_id = :empId AND e.punch_date BETWEEN :fromDate AND :toDate", nativeQuery = true)
    List<E_TimeSheet> findByEmpIdAndDateRange(@Param("empId") int empId,
                                              @Param("fromDate") LocalDate fromDate,
                                              @Param("toDate") LocalDate toDate);
    @Query(value = "SELECT * FROM timesheet_raw e " + "WHERE e.user_id = :empId " + "AND MONTH(e.punch_date) = :month "
            + "AND YEAR(e.punch_date) = :year", nativeQuery = true)
    List<E_TimeSheet> findByEmpIdAndMonth(@Param("empId") int empId, @Param("month") int month,
                                          @Param("year") int year);
}
