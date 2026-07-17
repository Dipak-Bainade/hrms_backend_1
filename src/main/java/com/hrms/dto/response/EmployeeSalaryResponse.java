package com.hrms.dto.response;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeSalaryResponse {

    private Long id;

    private Long employeeId;

    private String employeeCode;

    private String employeeName;

    private Long salaryStructureId;

    private String salaryStructureName;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean active;
    
    private Double grossSalary;

}