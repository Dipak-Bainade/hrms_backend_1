package com.hrms.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hrms.enums.AttendanceStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttendanceResponse {
	
	private Long id;

    private String employeeCode;

    private String employeeName;

    private LocalDate attendanceDate;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private Double workingHours;

    private AttendanceStatus status;

    private String remarks;

}
