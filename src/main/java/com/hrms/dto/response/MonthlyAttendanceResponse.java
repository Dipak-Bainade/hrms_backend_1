package com.hrms.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MonthlyAttendanceResponse {
	
	private Long employeeId;

    private String employeeCode;

    private String employeeName;

    private Integer month;

    private Integer year;

    private long totalPresentDays;

    private long totalAbsentDays;

    private long totalLeaveDays;

    private long totalHolidayDays;

    private long totalWeekOffDays;

    private long totalWorkFromHomeDays;

    private Double totalWorkingHours;

    private Double averageWorkingHours;

}
