package com.hrms.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendanceSummaryResponse {

    private Long presentDays;

    private Long absentDays;

    private Long leaveDays;

    private Long holidayDays;

    private BigDecimal totalWorkingHours;

    private BigDecimal averageWorkingHours;

}
