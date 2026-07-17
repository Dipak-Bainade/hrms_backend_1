package com.hrms.dto.payroll;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CtcReport {

    private String employeeCode;

    private String employeeName;

    private Double monthlyCTC;

    private Double annualCTC;

}
