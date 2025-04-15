package com.infotel.e_portal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@ToString
@Embeddable
public class E_TimeSheetIdentiy implements Serializable {

    @NotNull
    @Column(name = "User_ID")
    private Integer empId;

    @NotNull
    @Column(name = "Punch_Date")
    private LocalDate punchDate;

    @NotNull
    @Column(name = "Punch_Time")
    private LocalTime punchTime;
}
