package com.hrms.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayrollResponse {

    private Long payrollId;

    private Long employeeId;

    private String employeeCode;

    private String employeeName;

    private Integer month;

    private Integer year;

    private Integer workingDays;

    private Integer presentDays;

    private Integer leaveDays;

    private Integer lopDays;

    private Double overtimeHours;

    private Double grossSalary;

    private Double totalAllowance;

    private Double totalDeduction;

    private Double overtimeAmount;

    private Double lopDeduction;

    private Double netSalary;
    
    private Boolean processed;

    private LocalDateTime processedAt;

}