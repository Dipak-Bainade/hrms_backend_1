package com.hrms.dto.payroll;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepartmentPayrollReport {

    private String departmentName;

    private Integer employeeCount;

    private Double grossSalary;

    private Double deduction;

    private Double netSalary;

}