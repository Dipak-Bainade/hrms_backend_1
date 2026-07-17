package com.hrms.dto.payroll;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlyPayrollReport {

    private Integer month;

    private Integer year;

    private Integer totalEmployees;

    private Double grossSalary;

    private Double deductions;

    private Double netSalary;

}