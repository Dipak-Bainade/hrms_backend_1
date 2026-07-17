package com.hrms.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SalaryStructureResponse {

    private Long id;

    private String structureName;

    private Double basicSalary;

    private Double hra;

    private Double da;

    private Double specialAllowance;

    private Double otherAllowance;

    private Double pf;

    private Double professionalTax;

    private Double otherDeduction;

    private Double grossSalary;

    private Double totalDeduction;

    private Double netSalary;

}