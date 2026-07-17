package com.hrms.dto.payroll;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendanceSummary {

    private Integer workingDays;

    private Integer presentDays;

    private Integer approvedLeaveDays;

    private Integer lopDays;

    private Double overtimeHours;

}
