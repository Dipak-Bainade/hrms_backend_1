package com.hrms.dto.payroll;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayrollCalculationResult {

    private Double grossSalary;

    private Double allowance;

    private Double deduction;

    private Double lopDeduction;

    private Double overtimeAmount;

    private Double netSalary;

}
