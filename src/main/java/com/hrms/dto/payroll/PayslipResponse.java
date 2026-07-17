package com.hrms.dto.payroll;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayslipResponse {

    // Company
    private String companyName;
    private String companyAddress;
    private String companyLogo;

    // Employee
    private String employeeCode;
    private String employeeName;
    private String designation;
    private String department;

    // Payroll
    private Integer month;
    private Integer year;

    private Integer workingDays;
    private Integer presentDays;
    private Integer leaveDays;
    private Integer lopDays;

    // Earnings
    private Double basicSalary;
    private Double hra;
    private Double da;
    private Double specialAllowance;
    private Double otherAllowance;
    private Double overtimeAmount;

    // Deductions
    private Double pf;
    private Double professionalTax;
    private Double otherDeduction;
    private Double lopDeduction;

    // Totals
    private Double grossSalary;
    private Double totalDeduction;
    private Double netSalary;

}
