package com.infotel.e_portal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "timesheet_raw")
@IdClass(E_TimeSheetIdentiy.class)
public class E_TimeSheet {

    @Id
    @Column(name = "User_ID")
    private Integer empId;

    @Id
    @Column(name = "Punch_Date")
    private LocalDate punchDate;

    @Id
    @Column(name = "Punch_Time")
    private LocalTime punchTime;

    @Column(name = "In_Out_Type")
    private boolean inOutType;

    @Column(name = "Event_Status")
    private boolean eventStatus;

}
